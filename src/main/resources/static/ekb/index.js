layui.extend({
    ekb: 'lay/modules/ekb',
    validate: 'lay/modules/validate'
}).define(['ekb', 'conf'], function (exports) {
    layui.ekb.initPage();
    exports('index', {});
});