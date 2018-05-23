'use strict';

(function() {
	window.MainComponent = {
		_inputUrlRef: null,
		_resultRef: null,
		_currentResult: null,

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
		},
		
		_onSubmit: function(event) {
			event.preventDefault();
			var self = this;
		
			$.post('/api/urls', {
				url: this._inputUrlRef.value
			}).done(function(data) {
				self._showShortUrl(data.shortUrl);
			}).fail(function(e) {
				if (e.status == 404) {
					self._showError('올바른 URL을 입력해주세요.');
				} else {
					if (e.responseText) {
						self._showError(`<pre>다음과 같은 이유로 단축 URL을 생성하지 못하였습니다:\ncode = ${e.status}, message = ${e.responseText}</pre>`);
					} else {
						self._showError(`<pre>다음과 같은 이유로 단축 URL을 생성하지 못하였습니다:\ncode = ${e.status}, message = (없음))</pre>`);
					}
				}
			});
		},

		_showShortUrl: function(url) {
			if (this._currentResult) {
				this._resultRef.classList.remove(this._currentResult);
			}

			this._resultRef.style.display = 'block';
			this._currentResult = 'alert-primary';
			this._resultRef.classList.add(this._currentResult);
			this._resultRef.innerHTML = `<pre>단축 URL이 생성되었습니다.\n<a href="${url}">${url}</a></pre>`;
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
