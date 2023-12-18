define(
	['underscore', 'backbone', 'text!../tmpls/changelog.html'],
	function (_, Backbone, ChagelogTmpl) {
		'use strict';
		
		var ChangelogView = Backbone.View.extend({
			el: '#main',
			tagName: 'main',
			template: _.template(ChagelogTmpl),

			events: {},

			initialize: function() {
				this.change_menu_state();
				this.render();
			},

			render: function() {
				this.$el.html(this.template());
			    return this;
			},

			change_menu_state: function() {
				$("a.list-group-item").removeClass("active");
			}
		});
		
		return ChangelogView;
	}
);
