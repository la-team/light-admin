$(function () {
    var previousPoint;
 

    //Display horizontal graph
    var d1_h = [];
    for (var i = 0; i <= 4; i += 1)
        d1_h.push([parseInt(Math.random() * 30),i ]);

    var d2_h = [];
    for (var i = 0; i <= 4; i += 1)
        d2_h.push([parseInt(Math.random() * 30),i ]);

    var d3_h = [];
    for (var i = 0; i <= 4; i += 1)
        d3_h.push([ parseInt(Math.random() * 30),i]);
                
    var ds_h = new Array();
    ds_h.push({
        data:d1_h,
        bars: {
            horizontal:true, 
            show: true, 
            barWidth: 0.2, 
            order: 1,
        }
    });
ds_h.push({
    data:d2_h,
    bars: {
        horizontal:true, 
        show: true, 
        barWidth: 0.2, 
        order: 2
    }
});
ds_h.push({
    data:d3_h,
    bars: {
        horizontal:true, 
        show: true, 
        barWidth: 0.2, 
        order: 3
    }
});

    //tooltip function
    function showTooltip(x, y, contents, areAbsoluteXY) {
        var rootElt = 'body';
	
        $('<div id="tooltip3" class="tooltip">' + contents + '</div>').css( {
            position: 'absolute',
            display: 'none',
            top: y - 35,
            left: x - 5,
            border: '1px solid #000',
            border: '1px solid #000',
            padding: '1px 6px',
			'z-index': '9999',
            'background-color': '#202020',
			'color': '#fff',
			'font-size': '11px',
			'border-radius': '2px',
			'-webkit-border-radius': '2px',
			'-moz-border-radius': '2px',
            opacity: 0.8
        }).prependTo(rootElt).show();
    }

$.plot($("#hBar"), ds_h, {
    grid:{
        hoverable:true
    }
});

//add tooltip event
$("#hBar").bind("plothover", function (event, pos, item) {
    if (item) {
        if (previousPoint != item.datapoint) {
            previousPoint = item.datapoint;
 
            //delete de prГ©cГ©dente tooltip
            $('.tooltip').remove();
 
            var x = item.datapoint[0];
 
            //All the bars concerning a same x value must display a tooltip with this value and not the shifted value
            if(item.series.bars.order){
                for(var i=0; i < item.series.data.length; i++){
                    if(item.series.data[i][3] == item.datapoint[0])
                        x = item.series.data[i][0];
                }
            }
 
            var y = item.datapoint[1];
 
            showTooltip(item.pageX+5, item.pageY+5,x + " = " + y);
 
        }
    }
    else {
        $('.tooltip').remove();
        previousPoint = null;
    }
 
});

 
});