define(['backbone', '../models/user'], function (Backbone, UserModel) {
	'use strict';

	var UsersCollection = Backbone.Collection.extend({
		url: 'http://localhost:9000/admin/api/v1/users',
		model: UserModel
	});
	
	return UsersCollection;
});
