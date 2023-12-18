define(
	['underscore', 'backbone', '../models/user', 'text!../tmpls/userDetail.html'],
	function (_, Backbone, UsersModel, UserDetailTmpl) {
		'use strict';
		
		var UserDetailView = Backbone.View.extend({
			el: "#app",
			events: {},
			tagName: 'div',		
			template: _.template(UserDetailTmpl),

			initialize: function(user_id) {
				var _self = this;
				this.user = undefined;
				this.change_menu_state();
				var userModel = new UsersModel({id: user_id});
				userModel.fetch({
					success: function(user) {
						_self.user = user;
						_self.render();
					},
					error: function(collection, response, options) {
						console.log(response);
						alert(response.responseText);
					}
				});
			},
			
			render: function(options) {
				this.$el.html( this.template({user: this.user}) );
			},

			change_menu_state: function() {
				$("li.nav-item a.active").removeClass("active");
				$("li.nav-item a[href$='#/users']").addClass("active");
			}
		});
		
		return UserDetailView;
	}
);
