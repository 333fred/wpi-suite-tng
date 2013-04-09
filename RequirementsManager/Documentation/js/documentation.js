$(function(){
    $.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});
    $('#Menu').bind('tree.click', function(event) {
        // The clicked node is 'event.node'
        var node = event.node;
        loadNode(node, false);
    });
})

function loadNode(node,reload) {
        console.debug("tree.select: node.name = " + node.name);
        $tree.tree('selectNode', node, true);
        var fileLocation = "";
        var id = node.id;

        if(node.folder == true) {
            fileLocation = node.name.split(' ').join('_')+"/index.html";
        } else {
            fileLocation = node.name.split(' ').join('_')+".html";
        }

         while(node.parent.name){
            node = node.parent;
            fileLocation = node.name.split(' ').join('_')+"/"+fileLocation;
        }           

        fileLocation = "data/"+fileLocation;
        filePath = fileLocation.substring(0, fileLocation.lastIndexOf("/"));

        console.log("filePath= " + filePath + " , fileLocation= " + fileLocation);
        //Load content of page
        var url = "http://"+location.host + location.pathname + "?content="+fileLocation + "&lastnode="+id;
        console.log("url =  "+ url)
        if(url == location.href && !reload)
            return;
                        



        $('#Content').load(fileLocation, function () {
            console.debug("node.name = " + node.name);
 

            var nestedCall=false;
            $('.firstNode').each(function() {
                var firstChild=node.id+"0";
                console.debug("First Child Node is " + firstChild);
                firstChildNode = $tree.tree('getNodeById', firstChild); 
                loadNode(firstChildNode,true);
                nestedCall=true;
            });
            if(nestedCall) return;

            $('.titlebox .subtitle').html(node.name);
            // add local image path
            $('.figure img').attr('src', function() {
                 return location.pathname + filePath + "/" + this.getAttribute('data-path');
            });            
            //wrap figures
            $('.figure img').click( function() {
                var imgURL=$(this).attr("src");
                var $dialog =$('<div>/div>').html(
                        '<img src = ' + $(this).attr("src") + ' />').dialog({
                    modal: true,
                    width: 'auto', 
 //                   title: 'Full Size Image'
                });
                return false;
            });
            var anchor = $('a[nodeid]');
            console.log("#anchors = " + anchor.length);
            anchor.each(function() {
                console.log("in each()");
                aNodeId=$(this).attr('nodeId');
                console.log(aNodeId);              
                aNode = $tree.tree('getNodeById', aNodeId);

                console.log("checking anchor text: " + $(this).html());
                if($(this).html() == 0) {
                   $(this).html(aNode.name);
                }
            });

            anchor.click(function() {
                nodeId = $(this).attr('nodeId');
                node = $tree.tree('getNodeById', nodeId);
            //    parentId=nodeId.substring(0,nodeId.length-2);
             //   console.log("node name is " + node.name + " parentId = " + parentId);
                loadNode(node);
                return false;
            }); 
         
            var code = $('.code');
            code.each(function(){
                var content = $(this).html();
                $(this).html("<pre>" + content + "</pre>");
            });
            console.log(".scrollTop()= " + $(this).scrollTop());
            $(this).scrollTop(0);

            history.pushState(null, null, url);
        });
    }