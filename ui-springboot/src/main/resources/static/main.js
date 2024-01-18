$(function(){
    
    const defaultHeaders = {'Content-Type': "application/json"};
    let basePath = window?.API_BASE_URL;

    if (!basePath) {
      basePath = window.location.protocol + "//" + window.location.host;
    }

    $.ajax({
        url: basePath + '/fruits',
        headers: defaultHeaders,
        dataType: "json",
    }).then(function(data) {
        console.log(data);
        let data_stringify = JSON.stringify(data);
        let data_json = JSON.parse(data_stringify);
        $.each(data_json, function(index, value) {
            console.log(value);
            $('.scene').append('<div>'+ value.id + ':' + value.name + '</div>');
        })
    });
});