function MainComponent() {
}

MainComponent.prototype._inputUrlRef;
MainComponent.prototype._shortUrlRef;
MainComponent.prototype._messageRef;

MainComponent.prototype.init = function() {
	var self = this;

	$('#submit').click(function(event) {
		self.onSubmit(event);
	});

	this._inputUrlRef = $('#input-url')[0];
	console.log(this._inputUrlRef.value);
}

MainComponent.prototype.onSubmit = function(event) {
	event.preventDefault();

	$.post('/urls', {
		url: this._inputUrlRef.value
	}).done(function(data) {
		console.log(data);
	}).fail(function(e) {
		console.log(e);
		if (e.status) {
			console.log(e.responseText);
		}
	});
}
