'use strict';

var color = () => {

  var getBgColorRGBText = element => {
    return window.getComputedStyle(element)['background-color'];
  }

  var getBgColorRGB = element => {
    var bgColorRGBText = getBgColorRGBText(element);
    var rgbRegExp = new RegExp('^rgb\\((.*), (.*), (.*)\\)$');

    var red = bgColorRGBText.replace(rgbRegExp, '$1');
    var green = bgColorRGBText.replace(rgbRegExp, '$2');
    var blue = bgColorRGBText.replace(rgbRegExp, '$3');
   
    return { red, green, blue };
  }


  var getBgColorHex = element => {
    var {red, green, blue} = getBgColorRGB(element);
    return "#" 
        + decimaToHex(red) 
        + decimaToHex(green) 
        + decimaToHex(blue);
     
  }


  var decimaToHex = text => {
    var hex = Number(text).toString(16);
    
    return (hex.length < 2) ? "0" + hex : hex;
  } 
  
  return Object.freeze({
      getBgColorRGB, 
      getBgColorHex
  });

};


if (window){
  window.color = color();
}