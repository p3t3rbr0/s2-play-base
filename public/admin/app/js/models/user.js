define(['backbone'], function (Backbone) {
	'use strict';

	var UserModel = Backbone.Model.extend({

		initialize: function() {
			this.on('invalid', function(model, error){
				console.log(error);
			});
		},
		
		urlRoot: 'http://localhost:9000/admin/api/v1/users',
		
		defaults: {
			id: null,
			uid: null,
			avatar: 'http://localhost:9000/assets/admin/app/img/default-avatar.png',
			username: null,
			email: null,
			first_name: null,
			last_name: null,
			created: null,
			last_visited: null,
			is_activated: false,
			is_admin: false
		},

		validate: function(attrs, options) {
			// validate username (min, max, chars)
			// validate email (min, max, chars)
			// validate first_name (min, max, chars)
			// validate last_name (min, max, chars)
			// validate password (min, max)
			// validate password_confirm (min, max)
			return 'Ololo';
		},

		saveUser: function(options) {
			this.save(options.data, {
				type: options.method || 'POST',

				success: function(model) {
					window.location.replace('#/users');
				},
				error: function(model, response, options) {
					console.log(response);
					alert(response.responseText);
				}
			}); 
			return false;
		},
	});
	
	return UserModel;
});
