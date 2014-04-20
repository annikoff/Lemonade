var http = require('http');
var url = require('url');
var htmlparser = require('htmlparser2');
var md5 = require('MD5');
var events = require('events');

var startUrl = '';

var parsed = [];
var result = [];

function start(currentUrl) {
    
    var req = http.get(currentUrl, function(res) {
      console.log('STATUS: ' + res.statusCode);
      console.log('HEADERS: ' + JSON.stringify(res.headers));
      res.setEncoding('utf8');
      var hash = md5(currentUrl);
      if (parsed[hash] == undefined) {
        parsed[hash] = {'url': currentUrl, 'statusCode': res.statusCode};
        console.log(parsed[hash]);
      }
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
    var parser = new htmlparser.Parser({
        base: '',
        onopentag: function(name, attribs){
            if(name === 'base'){
                this.base = attribs.href;
            }
            if(name === 'a'){
                if (this.base != '') {
                    var resolvedUrl = url.resolve(this.base, attribs.href.replace(/&amp;/, '&'));
                }else {
                    var resolvedUrl = url.resolve(startUrl, attribs.href.replace(/&amp;/, '&'));
                }
                var hash = md5(resolvedUrl);
                if (result[hash] == undefined && parsed[hash] == undefined) {
                    result[hash] = {'url': resolvedUrl};
                }
            }
        }
    });
    parser.write(body);
    parser.end();
}

start(startUrl);