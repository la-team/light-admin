	$(function () {
		var data = [];
		var series = Math.floor(Math.random()*10)+1;
		for( var i = 0; i<series; i++)
		{
			data[i] = { label: "Series"+(i+1), data: Math.floor(Math.random()*100)+1 }
		}
	
	$.plot($("#pie"), data, 
	{
			series: {
				pie: { 
					show: true,
					label: {
						show: false,
						formatter: function(label, series){
							return '<div style="font-size:11px;text-align:center;padding:4px;color:white;">'+label+'<br/>'+Math.round(series.percent)+'%</div>';
						},
						threshold: 0.1
					}
				}
			},
			legend: {
				show: true,
				noColumns: 1, // number of colums in legend table
				labelFormatter: null, // fn: string -> string
				labelBoxBorderColor: "#000", // border color for the little label boxes
				container: null, // container (as jQuery object) to put legend in, null means default on top of graph
				position: "ne", // position of default legend container within plot
				margin: [5, 10], // distance from grid edge to default legend container within plot
				backgroundColor: "#efefef", // null means auto-detect
				backgroundOpacity: 1 // set to 0 to avoid background
			},
			grid: {
				hoverable: true,
				clickable: true
			},
	});
	$("#interactive").bind("plothover", pieHover);
	$("#interactive").bind("plotclick", pieClick);
	
	});
	
	function pieHover(event, pos, obj) 
	{
		if (!obj)
					return;
		percent = parseFloat(obj.series.percent).toFixed(2);
		$("#hover").html('<span style="font-weight: bold; color: '+obj.series.color+'">'+obj.series.label+' ('+percent+'%)</span>');
	}
	function pieClick(event, pos, obj) 
	{
		if (!obj)
					return;
		percent = parseFloat(obj.series.percent).toFixed(2);
		alert(''+obj.series.label+': '+percent+'%');
	}

	
