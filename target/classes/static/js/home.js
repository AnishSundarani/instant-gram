$(document).ready(function() {
	
	$("#nav").load("nav");
	
	loadPosts();
		
});

function loadPosts() {
	$("#posts").html("");
	
	$.ajax({
		url: "/posts/followingPost",
		type: 'get',
		success: function(data) {
			data.sort(GetSortOrder("timestamp"));
			console.log(data);
			
			if(data.length==0) {
				$("#posts").html('<main>  <h2> No Posts to show </h2> <span> Seearch and follow people or </span> <span> Go to <a href="/profile/' + currentUser + '" style="color: red;"> profile </a> </span> </main>');
			}

			for (var i = 0; i < data.length; i++) {
				$("#posts")
						.append(
								'<div class="lazy card mx-auto custom-card mt-3" id="post"><div class="row post-header col-12 py-2 px-3"><div class="col-10 float-left ">'
								+ '<div class="row"><div class="col-2"><img id="sdp" class="pp" src="/default.png"></div>' + '<div class="col-10" id="uname"><a href="'
								+ '/profile/'+ data[i].user.username + '"><h4>@'
								+ data[i].user.username +
								'</h4></a></div></div></div></div><img class="card-img" src="'
								+ data[i].picPath +
								'" alt="Card image cap"> <br><div class="row post-header px-3 pb-3"><div class="col-1 float-left text-left"><i class="far fa-heart"></i></i></div><div class="col-10 float-left text-left">Comment...</div><div class="col-1 float-right text-right"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></div></div></div>');
			}
		}
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