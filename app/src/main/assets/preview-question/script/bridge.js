;(function(window) {
    var DEBUG = true;
    var callbacks = {};
    var guid = 0;
    var ua = navigator.userAgent;
    // TODO 精确性待改进
    var ANDROID = /android/i.test(ua);
    var IOS = /iphone|ipad/i.test(ua);
    var WP = /windows phone/i.test(ua);
    //ANDROID = 0; IOS = 1;

    /**
     * 方便在各个平台中看到完整的 log
     */
    function log() {
        if (DEBUG) {
            console.log.call(console, Array.prototype.join.call(arguments, ' '));
        }
    }

    /**
     * 平台相关的 Web 与 Native 单向通信方法
     */
    function invoke(cmd) {
        log('invoke', cmd);

        if (ANDROID) {
            prompt(cmd);
        }
        else if (IOS) {
            location.href = 'bridge://' + cmd;
        }
        else if (WP) {
            // TODO ...
            console.log(cmd)
        }
    }

    var Bridge = {
        callByJS: function(opt) {
            log('callByJS', JSON.stringify(opt));

            var input = {};
            input.name = opt.name;
            input.token = ++guid;
            input.param = opt.param || {};
            callbacks[input.token] = opt.callback;

            invoke(JSON.stringify(input));
        },
        callByNative: function(opt) {
            log('callByNative', JSON.stringify(opt));

            var callback = callbacks[opt.token];
            var ret = opt.ret || {};
            var script = opt.script || '';

            // Native 主动调用 Web
            if (script) {
                log('callByNative script', script);
                try {
                    invoke(JSON.stringify({
                        token: opt.token,
                        ret: eval(script)
                    }));
                } catch (e) {
                    console.error(e);
                }
            }
            // Web 主动调用 Native，Native 被动的响应
            else if (callback) {
                callback(ret);
                try {
                    delete callback;
                    log(callbacks);
                } catch (e) {
                    console.error(e);
                }
            }

        }
    };

    window.Bridge = Bridge;
    window.__log = log;
})(window);
