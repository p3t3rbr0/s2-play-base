define(
    ['underscore', 'backbone', 'text!../tmpls/index.html'],
    function (_, Backbone, IndexTmpl) {
        'use strict';
        
        var IndexView = Backbone.View.extend({
            el: '#main',
            tagName: 'main',
            template: _.template(IndexTmpl),

            events: {},

            initialize: function() {
                this.change_menu_state();
                this.render();
                return this;
            },

            render: function() {
                this.$el.html(
                    this.template({
                        version: $('#sidebar__footer').data('version')
                    })
                );
                return this;
            },

            change_menu_state: function() {
                $("a.list-group-item").removeClass("active");
                $("a.list-group-item[href$='#/index']").addClass("active");
            }
        });
        
        return IndexView;
    }
);
