layui.define(function(exports) {
  exports('conf', {
    container: 'ekb',
    containerBody: 'ekb-body',
    v: '2.0',
    base: layui.cache.base,
    css: layui.cache.base + 'css/',
    views: layui.cache.base + 'views/',
    viewLoadBar: true,
    debug: layui.cache.debug,
    name: 'ekb',
    entry: '/index',
    engine: '',
    eventName: 'ekb-event',
    tableName: 'ekb',
    requestUrl: './'
  })
});
