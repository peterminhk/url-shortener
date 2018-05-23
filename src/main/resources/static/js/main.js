'use strict';

(function() {
	window.MainComponent = {
		_inputUrlRef: null,
		_resultRef: null,
		_currentResult: null,
		_csrf_header: null,
		_csrf_token: null,

		init: function() {
			var self = this;

			$('form').submit(function(event) {
				self._onSubmit(event);
			});

			$('#input-url').keydown(function(event) {
				self._hideResult();
			});

			this._inputUrlRef = $('#input-url')[0];
			this._resultRef = $('.result')[0];
			this._csrf_header = $("meta[name='_csrf_header']").attr("content");
			this._csrf_token = $("meta[name='_csrf']").attr("content");
		},

		_onSubmit: function(event) {
			event.preventDefault();
			var self = this;

			$.post({
				url: '/api/urls',
				headers: {
					[this._csrf_header]: this._csrf_token
				},
				data: {
					url: this._inputUrlRef.value
				}
			}).done(function(data) {
				self._showShortUrl(data.shortUrl);
			}).fail(function(e) {
				if (e.status == 404) {
					self._showError('올바른 URL을 입력해주세요.');
				} else {
					if (e.responseText) {
						self._showError(`<pre>단축 URL 생성에 실패하였습니다.\n원인: code = ${e.status}, message = ${self._escapeHtml(e.responseText)}</pre>`);
					} else {
						self._showError(`<pre>단축 URL 생성에 실패하였습니다.\n원인: code = ${e.status}, message = (없음)</pre>`);
					}
				}
			});
		},

		_escapeHtml: function(text) {
			return text
				.replace(/</g, '&#60;')
				.replace(/>/g, '&#62;');
		},

		_showShortUrl: function(url) {
			if (this._currentResult) {
				this._resultRef.classList.remove(this._currentResult);
			}

			this._resultRef.style.display = 'block';
			this._currentResult = 'alert-primary';
			this._resultRef.classList.add(this._currentResult);
			this._resultRef.innerHTML = `<pre>단축 URL이 생성되었습니다.\n<a href="${url}">${this._escapeHtml(url)}</a></pre>`;
		},

		_showError: function(message) {
			if (this._currentResult) {
				this._resultRef.classList.remove(this._currentResult);
			}

			this._resultRef.style.display = 'block';
			this._currentResult = 'alert-danger';
			this._resultRef.classList.add(this._currentResult);
			this._resultRef.innerHTML = message;
		},

		_hideResult: function() {
			if (this._currentResult) {
				this._resultRef.classList.remove(this._currentResult);
			}

			this._resultRef.style.display = 'none';
			this._resultRef.innerHTML = '';
		}
	}
})();
