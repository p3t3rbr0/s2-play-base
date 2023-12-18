'use strict';

requirejs.config({
    'baseUrl': "assets/admin",
	'paths': {
		'text': 'deps/requirejs/text',
		'jquery': 'deps/jquery/jquery-3.4.1.min',
		'backbone': 'deps/backbone/backbone-min',
		'underscore': 'deps/underscore/underscore-min',
		'bootstrap': 'deps/bootstrap/js/bootstrap.bundle.min'
    },	
	'shim': {
		'backbone': {
            'deps': ['jquery', 'underscore'],
            'exports': 'Backbone'
        },
		'bootstrap': {
            'deps': ['jquery']
        },
	}
});

require(['underscore', 'backbone', 'bootstrap', 'app/js/routers/router'], function (_, Backbone, undefined, Router) {
	$.fn.serializeObject = function() {
		var o = {};
		$.each(this.serializeArray(), function() {
			if (typeof o[this.name] === 'undefined') {
				o[this.name] = this.value || '';
			} else {
				if (!o[this.name].push)
					o[this.name] = [o[this.name]];
				o[this.name].push(this.value || '');
			}
		});
		return o;
	};

	$.getCookie = function(name) {
		var matches = document.cookie.match(new RegExp(
			"(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
		));
		return matches ? decodeURIComponent(matches[1]) : undefined;
	};

	$.getP = function(opt){
		return new Promise(function(resolve,reject){
			$.get(opt.url,opt.data||{})
			 .done(function(resp){
				 if(resp.error) reject(resp.error);
				 resolve(resp);
			 }).fail(function(err){reject(err);});
		});
	};
	
	$.postP = function(opt){
		return new Promise(function(resolve,reject){
			$.post(opt.url,opt.data||{})
			 .done(function(resp){
				 if(resp.error) reject(resp.error);
				 resolve(resp);
			 }).fail(function(err){reject(err);});
		});
	};
	
	$.request = function(opt){
		return new Promise(function(resolve,reject){
			var ajax_options = {
				url: opt.url,
				type: opt.method || 'POST',
				data: opt.data || {}
			};
			for (var p in opt.options || {}) {
				if (opt.options.hasOwnProperty(p))
					ajax_options[p] = opt.options[p];
			}
			$.ajax(ajax_options).done(function(resp){
				if(resp && resp.error) reject(resp.error);
				resolve(resp || {});
			}).fail(function(err){reject(err);});
		});
	};

	Backbone.Model.prototype.get = function(attr) {
		if (attr.indexOf('.') === -1)
			return this.attributes[attr];
		return _.inject(attr.split('.'), function(o, k) { return o && o[k];	}, this.attributes);
	};

	new Router();		
	Backbone.history.start();
});
