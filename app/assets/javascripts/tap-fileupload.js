$('#fileref').on('change',function(e){
    //get the file name
    var fileName = e.target.files[0].name;
    //replace the "Choose a file" label
    $(this).next('.custom-file-label').html(fileName);
})
