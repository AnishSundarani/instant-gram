$(function() {
	$("#nav").load("/nav");
});

$(".postimg").Lazy({
    // your configuration goes here
    effect: 'fadeIn',
    effectTime: 1000,
    visibleOnly: true,
    onError: function(element) {
        console.log('error loading ' + element.data('src'));
    }
});

function follow() {

	$.ajax({
		url : '/follow/' + username,
		type : 'post',
		async : false,
		success : function(data) {
			console.log(data);
			$("#followBtn").hide();
			$("#unfollowBtn").show();

			loadFollowers();
		},
		cache : false,
		contentType : false,
		processData : false
	});

}

function unfollow() {
	$.ajax({
		url : '/follow/' + username,
		type : 'delete',
		async : false,
		success : function(data) {
			console.log(data);
			$("#followBtn").show();
			$("#unfollowBtn").hide();

			loadFollowers();
		},
		cache : false,
		contentType : false,
		processData : false
	});
}

function GetSortOrder(prop) {
	return function(a, b) {
		if (a[prop] > b[prop]) {
			return -1;
		} else if (a[prop] < b[prop]) {
			return 1;
		}
		return 0;
	}
}

$(document).ready(function() {

	$("#followBtn").show();
	$("#unfollowBtn").hide();

	putLoader('loader_div');

	loadPosts();

	loadFollowers();

	loadFollowing();

});

function loadFollowers() {
	$
			.ajax({
				url : '/follow/followers/' + username,
				type : 'get',
				async : false,
				success : function(data) {
					console.log(data);
					window.followers = data;
					$("#followerCount").html(data.length);
					$("#followerList").html("");
					for ( var i in data) {
						$("#followerList")
								.append(
										'<a href="/profile/'
												+ data[i].username
												+ '" class="list-group-item list-group-item-action">'
												+ data[i].fullName + '</a>');
						if (data[i].username == currentUser) {
							$("#followBtn").hide();
							$("#unfollowBtn").show();
							break;
						}
					}
				},
				cache : false,
			});

}

function loadFollowing() {
	$
			.ajax({
				url : '/follow/following/' + username,
				type : 'get',
				async : false,
				success : function(data) {
					console.log(data);
					window.following = data;
					$("#followingCount").html(data.length);
					$("#followingList").html("");
					for ( var i in data) {
						$("#followingList")
								.append(
										'<a href="/profile/'
												+ data[i].username
												+ '" class="list-group-item list-group-item-action">'
												+ data[i].fullName + '</a>');
					}
				},
				cache : false,
			});

}

function loadPosts() {
	$("#posts").html("");
	$
			.ajax({
				url : '/posts/' + username,
				type : 'get',
				success : function(data) {

					data.sort(GetSortOrder("timestamp"));
					console.log(data);

					$("#postCount").html(data.length);
					for (var i = 0; i < data.length; i++) {
						$("#posts")
								.append(
										' <div class="col-4 post"><img id="'
												+ data[i].id
												+ '" class="postimg" src="'
												+ data[i].picPath
												+ '"  onclick="changeModal(this);" data-toggle="modal" data-target="#postModal" /></div>');
					}
				},
				fail : function(data) {
					console.log("fetch failed" + data);
				}
			});

}

function changeModal(post) {
	$("#modal_image").attr("src", post.src);
	window.mpostid = post.id;
	console.log(post.id);

}

$("form#upload").submit(function(e) {
	e.preventDefault();
	var formData = new FormData(this);

	if (confirm("Are you sure you want to upload this picture?")) {

		showLoader();

		$.ajax({
			url : '/posts/',
			type : 'post',
			data : formData,
			async : false,
			success : function(data) {
				console.log(data);
				loadPosts();
				hideLoader();
			},
			cache : false,
			contentType : false,
			processData : false
		});
	}
});

function deletePost() {

	if (confirm("Are you sure you want to delete this picture?")) {
		$.ajax({
			url : "/posts/" + window.mpostid,
			type : 'delete',
			async : false,
			success : function(data) {
				console.log(data);
			}
		});

		location.reload();
	}
}
