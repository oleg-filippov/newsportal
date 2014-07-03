$(document).ready(function() {	
	// Ajax request for tags json
	$.get('tags/autocomplete', function (data) {
		tagsAutocomplete(JSON.parse(data));
	});
});

//Tokenfield tags using Twitter Typeahead Bloodhound autocomplete
function tagsAutocomplete(tags) {
	var engine = new Bloodhound({
		datumTokenizer: function(d) {
			return Bloodhound.tokenizers.whitespace(d.value); 
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		local: $.map(tags, function(tag) { return { value: tag }; })
	});
	engine.initialize();

	$('#tags').tokenfield({
		limit: 5,
		minLength: 2,
		typeahead: {
			source: engine.ttAdapter()
		}
	});
}
		
// Upload image in the editor
function sendFile(file, editor, welEditable) {
	data = new FormData();
	data.append("file", file);
	$.ajax({
		data: data,
		type: "POST",
		url: 'uploadimage',
		cache: false,
		contentType: false,
		processData: false,
		success: function(response) {
			if (/^images/.test(response)) {
				editor.insertImage(welEditable, response);
				$("#resp").hide();
			} else {
				$("#resp").text(response).show();
			}
		}
	});
}