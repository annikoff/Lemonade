var http = require('http');
var url = require('url');
var htmlparser = require('htmlparser2');
var md5 = require('MD5');
var events = require('events');
var eventEmitter = new events.EventEmitter();

var startUrl = '';

var parsed = [];
var result = [];
var maxThreads = 5;
var threads = 0;

function start(inputUrl, id) {
    var currentUrl = inputUrl.url;
    var paresedUrl = url.parse(currentUrl);

    if (paresedUrl.protocol != 'http:') {
        return;
    }
    var options = {
        host: paresedUrl.host,
        port: '80',
        path: paresedUrl.path,
        method: 'GET'
    }

    var req = http.request(options, function(res) {
      //console.log('STATUS: ' + res.statusCode);
      //console.log('HEADERS: ' + JSON.stringify(res.headers));
      res.setEncoding('utf8');
      var hash = md5(currentUrl);
      if (parsed[hash] == undefined) {
        parsed[hash] = {'url': currentUrl, 'statusCode': res.statusCode, 'referrer': []};
        console.log(id+" | "+res.statusCode+" | "+currentUrl);
      }

      var html = "";

      res.on('data', function (chunk) {
            html += chunk;
      });

      res.on('end', function () {
        if (res.statusCode == 200 || res.statusCode == 404) {
            parseHtml(html, currentUrl);
            parsed[hash]['html'] = html;
            parsed[hash]['referrer'].push(inputUrl.referrer);
        }
        threads--;
        eventEmitter.emit('threadDone');
      });

    });

    req.on('error', function(e) {
      console.log('problem with request: ' + e.message);
    });

    req.end();
}

function parseHtml(body, referrer) {
    var parser = new htmlparser.Parser({
        base: '',
        onopentag: function(name, attribs){
            if(name === 'base'){
                this.base = attribs.href;
            }
            if(name === 'a' && attribs.href != undefined){
                if (this.base != '') {
                    var resolvedUrl = url.resolve(this.base, attribs.href.replace(/&amp;/, '&'));
                }else {
                    var resolvedUrl = url.resolve(startUrl, attribs.href.replace(/&amp;/, '&'));
                }
                var hash = md5(resolvedUrl);
                if (result[hash] == undefined) {
                    if (parsed[hash] == undefined) {
                        result[hash] = {'url': resolvedUrl, 'referrer': referrer};
                    }else {
                        parsed[hash]['referrer'].push(referrer);
                    }
                }
            }
        }
    });
    parser.write(body);
    parser.end();
}

function hasHash(array, hash) {
    var check = array.filter(function(array) {
        return array.hash === hash;
    });

    if (check.length) {
        return true;
    }else {
        return false;
    }
}

eventEmitter.on('threadDone', function() {
    for (hash in result) {
        if (threads < maxThreads) {
            threads++;
            start(result[hash], threads);
            delete result[hash];
        }else {
            break;
        }
    }
});
threads++;
var hash = md5(startUrl);
result[hash] = {'url': startUrl, 'referrer': ''}
start(result[hash], 1);
setInterval(function () {
    //console.log(Object.keys(parsed).length+' | '+Object.keys(result).length);
}, 10);