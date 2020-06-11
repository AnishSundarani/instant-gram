function putLoader(div_id,overlay_id='overlay'){
    str = `
        <div id="`+overlay_id+`" style="display:none;"> 
            <div class="spinner-border text-primary" role="status">
                <span class="sr-only">Loading...</span>
            </div>
        </div>
    `;

    $('#'+div_id).html(str);
}

function showLoader(overlay_id='overlay'){
    $('#'+overlay_id).fadeIn();
}

function hideLoader(overlay_id='overlay'){
    $('#'+overlay_id).fadeOut();
}
