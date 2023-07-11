const functions = require('firebase-functions');
const admin=require('firebase-admin');
const nodemailer =require('nodemailer');
const PDFDocument = require('pdfkit');
const fs = require('fs');
const os = require('os');
const path = require('path');

//sha1
const crypto = require("crypto");
function sha1(data) {
    return crypto.createHash("sha1").update(data, "binary").digest("hex");
}



admin.initializeApp()
require('dotenv').config()

const {SENDER_EMAIL,SENDER_PASSWORD}= process.env;

exports.sendCerf=functions.firestore.document('gencerf/{docId}')
.onCreate((snap,ctx)=>{
	console.log('Function started.................................');
    const data=snap.data();
    let authData=nodemailer.createTransport({
        host:'smtp.gmail.com',
        port:465,
        secure:true,
        auth:{
            user:SENDER_EMAIL,
            pass:SENDER_PASSWORD
        }
    });


	var name = `${data.name}`
	var init = `${data.init}`
	var date = `${data.date}`
	var lang = `${data.lang}`
	var email = `${data.email}`

	

	var stringtohash =name+" "+date
	var hash=sha1(stringtohash)
	var nametodis = name+" "+init

	// Create a document
	const doc = new PDFDocument({ size: [842,595] });

    const fileName = 'Certificate.pdf';
    const tempFilePath = path.join(os.tmpdir(), fileName);
    
    // output file
	doc.pipe(fs.createWriteStream(tempFilePath));


	// Add an image
	switch(lang) {
	  case "c":
	    doc.image('c.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "c++":
	    doc.image('cplus.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "java":
	    doc.image('java.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "linux":
	    doc.image('linux.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "perl":
	    doc.image('perl.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "php":
	    doc.image('php.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "python":
	    doc.image('python.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  case "scilab":
	    doc.image('scilab.jpg', 0, 0, {width: 842,  height: 595});
	    break;
	  default:
	    console.log('Error Lang');
	}

	doc
	  .font('Times-Roman')
	  .fillColor('#2BB7B0')
	  .fontSize(20)
	  .text(nametodis, 450, 260);

	doc
	  .font('Times-Roman')
	  .fillColor('#000000')
	  .fontSize(20)
	  .text(date, 290, 420);


	doc
	  .font('Times-Roman')
	  .fillColor('#000000')
	  .fontSize(2)
	  .text(hash,420, 305);



	// Finalize PDF file
	doc.end();


	authData.sendMail({
	from :'info.truly@makethatapp.com',
	to:email,
	subject:'Congratulations For Passing The Exam ',
	text:"You've done it!  Your patience and persistence has finally paid off!!!",
	attachments: [
	        {   // utf-8 string as an attachment
	            filename: 'Certificate.pdf',
	            path: tempFilePath // stream this file
	        }]
	}).then(res=>console.log('successfully sent  Cerf to '+`${data.email}`)).catch(err=>console.log(err));

});