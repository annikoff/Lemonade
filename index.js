var http = require("http");
var url = require("url");
var htmlparser = require("htmlparser2");
var crypto = require('crypto');
var md5 = crypto.createHash('md5');
var startUrl = url.parse("http://yournewbusiness.ru/sitemap?test&тест");

var results;

function start(parsedUrl) {
    
    var options = {
      hostname: parsedUrl.host,
      port: 80,
      path: parsedUrl.path,
      method: 'GET'
    };

    var req = http.request(options, function(res) {
      console.log('STATUS: ' + res.statusCode);
      console.log('HEADERS: ' + JSON.stringify(res.headers));
      res.setEncoding('utf8');
      res.on('data', function (body) {
        parseHtml(body);
      });
    });

    req.on('error', function(e) {
      console.log('problem with request: ' + e.message);
    });

    req.end();
}

function parseHtml(body) {
    var base = "";
    var parser = new htmlparser.Parser({
        onopentag: function(name, attribs){
            if(name === "base"){
                base = attribs.href;
                console.log("base:" + attribs.href);
            }
            if(name === "a"){
                console.log(base);
                if (base != "") {
                    console.log(md5.update(attribs.href) + url.resolve(base, attribs.href));
                }else {
                    console.log(md5.update(attribs.href) + url.resolve(startUrl.href, attribs.href));
                }
            }
        }
    });
    parser.write(body);
    parser.end();
}

start(startUrl);
