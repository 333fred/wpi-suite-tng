window.onload = function(){
    //Initialize Tree
    $tree = $('#Menu');
    $tree.tree({
        data : data,
        selectable: true
    });


    console.log("onload called");
    var path = getURLParam("content");
    var nodeId = getURLParam("lastnode");
 //   loadPage(path, nodeId);
    if(nodeId)
        loadPageById(nodeId);

    //Load Parameterized content values for open node and content
    //Allows for back&forward and direct linking support

    //Event handlers for back & forward buttons
    window.addEventListener("popstate", function(e){
        console.log("in popstate listener");
        var path = getURLParam("content");
        nodeId = getURLParam("lastnode");
//        loadNode(node);
        if(nodeId)
            loadPageById(nodeId);
    });	

    function loadPage(path,nodeId) {
        if(path != ""){
            $('#Content').load(path);
        }
        console.log("selecting node " + nodeId)
         if(nodeId != "") { 
            node = $tree.tree('getNodeById', nodeId);
            $("p").html(node.name);
            $tree.tree('selectNode', node, true);
            $('.titlebox .subtitle').html(node.name);
        }

    }
    function loadPageById(nodeId) {
         console.log("loadPagyById for id= " + nodeId)
         if(nodeId != "") { 
            node = $tree.tree('getNodeById', nodeId);  
            loadNode(node, true);     
        }
    }
               
}