var openLoader = function(self){
    console.log("self:",self);
    const temp =document.createElement("div");
    temp.innerHTML=  `
    <div style="width:100%;height:100%;background: url(../jQuery/images/ajax-loader.gif);position:fixed;margin:auto;">正在加载。。。</div>
    `;
    const body = document.getElementsByClassName("ztcontainer")[0];
    console.log("body:",body);
    body.appendChild(temp);
}
