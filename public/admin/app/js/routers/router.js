define(
	[
        'backbone',
        'app/js/views/index',
        'app/js/views/changelog',
        /* 'app/js/views/usersList',
         * 'app/js/views/userDetail',
         * 'app/js/views/createUser' */
    ],
	function(
        Backbone,
        IndexView,
        ChangelogView
    ) {
		'use strict';
		
		var Router = Backbone.Router.extend({
			routes: {
                '': 'showIndex',
                'index': 'showIndex',
                'changelog': 'showChangelog',
			},

            showIndex: function() { new IndexView(); },
            showChangelog: function() { new ChangelogView(); },
		});

		return Router;
	}
);
