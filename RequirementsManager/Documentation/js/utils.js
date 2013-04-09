function getURLParam(name) {
    // get query string part of url into its own variable
    var params = [];
    var url = window.location.href;
    var query_string = url.split("?");

    // make array of all name/value pairs in query string
    if(typeof query_string[1] !== "undefined")
        var params = query_string[1].split("&");

    // loop through the parameters
    var i = 0;
    while (i < params.length) {
        // compare param name against arg passed in
        var param_item = params[i].split("=");
        if (param_item[0] == name) {
            // if they match, return the value
            return param_item[1];
        }
        i++;
    }
    return "";
}

function processFigure() {
    return '<a href =' + $(this).attr("src") + '/>';
}