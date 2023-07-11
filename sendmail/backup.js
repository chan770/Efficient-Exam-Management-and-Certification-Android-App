const functions = require('firebase-functions');
const admin=require('firebase-admin');
const nodemailer =require('nodemailer');
admin.initializeApp()
require('dotenv').config()

const {SENDER_EMAIL,SENDER_PASSWORD}= process.env;

exports.sendCerf=functions.firestore.document('gencerf/{docId}')
.onCreate((snap,ctx)=>{
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



	authData.sendMail({
	from :'info.truly@makethatapp.com',
	to:`${data.email}`,
	subject:'Congratulations on completing the Exam',
	text:`${data.email}`,
	html:`${data.email}`,
	attachments: [
	        {   // utf-8 string as an attachment
	            filename: 'Certificate.pdf',
	            path: 'https://firebasestorage.googleapis.com/v0/b/onemarkexam.appspot.com/o/output.pdf?alt=media&token=095d4b71-490e-406d-9920-80ddff0559a7'
	        }]
	}).then(res=>console.log('successfully sent  Cerf to '+`${data.email}`)).catch(err=>console.log(err));

});
