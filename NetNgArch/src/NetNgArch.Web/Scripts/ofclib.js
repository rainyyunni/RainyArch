function ShowOFC(id, getData, width, height) {
    swfobject.embedSWF("/Scripts/open-flash-chart.swf", id, width, height, "9.0.0", "expressInstall.swf", { "get-data": getData }, { wmode: "opaque" });
}

function ofc_ready() {
   
}
