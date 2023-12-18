define(
	[
		'underscore',
		'backbone',
		'../models/user',
		'text!../tmpls/createUser.html',
		'text!../tmpls/error.html'
	],
	function (_, Backbone, UserModel, CreateUserTmpl, ErrorTmpl) {
		'use strict';
		
		var CreateUserView = Backbone.View.extend({
			el: "#app",
			events: {
				'click #create-user-btn': 'createUser'
			},
			tagName: 'div',		
			template: _.template(CreateUserTmpl),
			error_template: _.template(ErrorTmpl),

			initialize: function(user_id) {
				var _self = this;
				this.user = undefined;
				this.change_menu_state();
				this.render();
			},
			
			render: function(options) {
				this.$el.html( this.template({user: this.user}) );
			},

			change_menu_state: function() {
				$("li.nav-item a.active").removeClass("active");
				$("li.nav-item a[href$='#/users']").addClass("active");
			},

			createUser: function(event) {
				this.undelegateEvents();

				var formData = $('form').serializeObject();
				console.log("FORM_DATA: ", formData);

				var user = new UserModel(formData);
				console.log("USER: ", user);
				
				if (user.isValid()) {
					console.log("VALID");
					user.saveUser({data: formData});
				} else {
					console.log("INVALID");
					console.log(user.validationError);
					this.error_template({error_title: 'Error!', error_message: user.validationError});
					$('#error-modal').modal('toggle');
				}

				return false;
			}
		});
		
		return CreateUserView;
	}
);

