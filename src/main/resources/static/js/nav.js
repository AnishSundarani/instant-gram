$('#userSearch').autocomplete({
	select : loadProfile,
});

function loadProfile(event, ui) {
	var val = ui.item.label;
	window.location = "/profile/"+val.substring(val.indexOf("@")+1, val.length-1);
}

userSearch.oninput = function() {
	$.ajax({
		url : "/getusers/",
		type : "get",
		data : {
			"subStrUser" : userSearch.value,
		},
		success : function(data) {
			var arr = [];
			for (var i = 0; i < data.length; i++) {
				arr.push(data[i].fullName + " (@"+ data[i].username + ")");
			}
			console.log(arr);
			$("#userSearch").autocomplete({
				source : arr,
			});
		},
	});
};