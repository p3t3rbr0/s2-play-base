define(
	['underscore', 'backbone', '../collections/users', 'text!../tmpls/usersList.html'],
	function (_, Backbone, UsersCollection, UsersListTmpl) {
		'use strict';
		
		var UsersListView = Backbone.View.extend({
			el: "#app",
			events: {},
			tagName: 'div',		
			template: _.template(UsersListTmpl),

			initialize: function() {
				var _self = this;
				this.users = undefined;
				this.change_menu_state();
				
				var usersList = new UsersCollection();
				usersList.fetch({
					success: function(users) {
						_self.users = users.models;
						_self.render();
					},
					error: function(collection, response, options) {
						alert(response.responseText);
					}
				});
			},
			
			render: function(options) {
				this.$el.html( this.template({users_list: this.users}) );
			},

			change_menu_state: function() {
				$("li.nav-item a.active").removeClass("active");
				$("li.nav-item a[href$='#/users']").addClass("active");
			}
		});
		
		return UsersListView;
	});
