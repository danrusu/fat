/*****
* Module: Reports management
*
* Dependencies: jQuery
*****/
'use strict'
var DEBUG = false;


const log = text => {
  if (DEBUG){ console.log(text) }
}



const resultsPageSetup = () => {

  const rowHighlightColor = '#d7ffb3'; // rgb(215,255,179)


  // trick to position Details in the center ....modified info svf has long length
  $('#showDetails').parents('th').append( ( () => { 
    const newSvg  = $('#showDetails').parent().clone()
    newSvg.css('opacity','0').attr('title', '') 
    return newSvg;
  })() )
	
  // succeeded -> green color; failed -> red color
  if ($('#suiteResultsContainer td:nth-of-type(3) font').text()=="Succeeded"){
    $('#suiteResultsContainer td:nth-of-type(3), #suiteResultsContainer th:nth-of-type(3)')
      .css('background','rgb(0, 128, 0)')
      .find('font').css('color','rgb(255, 255, 255)');

  }else{
    $('#suiteResultsContainer td:nth-of-type(3), #suiteResultsContainer th:nth-of-type(3)')
      .css('background','rgb(255, 0, 0)')
      .find('font').css('color','rgb(255, 255, 255)');
  }
	
  $('.results td:first-child:not(:contains("/"))')
    .parent().children().css({'border-top': '3px solid #0868a1', 'font-weight':'bold'});

  // colapse tescases within test on test row click
  const $testRows = $('.results tr td:first-child:not(:contains("/"))').parent();

  $testRows
  
	.click( function() {
      const i = $.trim($(this).find('td:first-child').text().split('/')[0]);
      $('.results td:first-child:contains("'+ i +'/")').parent().slideToggle("fast");
    })

    .attr('title', 'Collapse/Expand test cases')

    .mouseenter( function() {$(this).children().css('opacity',0.7)} )

    .mouseleave( function() {$(this).children().css('opacity',1)} )
    
    .find('td').css({'background-color':'#ceecfd', 'cursor':'pointer'});
		
  // highlight test-cases rows	
  const $testCasesRows = $('.results tr td:first-child:contains("/")').parent();
  
  var defaultColor;
  
  $testCasesRows.mouseenter(function(){
    defaultColor = $(this).children('td').eq(1).css('background-color');
    $(this).children('td').css('background-color', rowHighlightColor);
  })
  
  $testCasesRows.mouseleave(function(){
    $(this).children('td').css('background-color', defaultColor);
  });
		
		
  localStorage.setItem("showDetails", "false");
  var $showDetails =  $('#showDetails');
  $showDetails.attr('title', 'Show test cases info')
  
    .click( () => {
      if(localStorage.getItem("showDetails") == "false"){
        if(localStorage.getItem("colapsedAll") == "true"){
          $('#expandTestcases').click()
        }
        $('div.testCaseDocs').show()
        localStorage.setItem("showDetails", "true")
      } else {
        $('div.testCaseDocs').hide()
        localStorage.setItem("showDetails","false")
      }      
    });
	   
	          
  var $detailsCell = $('table.results td:last-child');
  $detailsCell.css({'line-height':'1.5em'});


  // click + to expand/collapse all testcases
  localStorage["colapsedAll"] = "true";
  $('#expandTestcases').click( () => {
    if(localStorage["colapsedAll"] == "false"){
      $('.results td:first-child:contains("/")').parent().hide();
      localStorage["colapsedAll"] = "true";
    } else {
      $('.results td:first-child:contains("/")').parent().show();
      localStorage["colapsedAll"] = "false";
    }
 })


  $( document ).ready( () => {
    $('#expandTestcases').click();
  })

}

