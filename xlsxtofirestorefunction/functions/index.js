
const UUID = require("uuid-v4");
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { Storage } = require('@google-cloud/storage');
const projectId = 'onemarkexam';
const os = require('os');
const path = require('path');
const spawn = require('child-process-promise').spawn;
const cors = require('cors')({ origin: true });
const Busboy = require('busboy');
const fs = require('fs');
const keyFilename = "onemarkexam-64be91e48160.json";
var xlsx_json = require('./libs');

admin.initializeApp();


let gcs = new Storage({
    projectId: projectId,
    keyFilename: keyFilename
});

exports.onFileChange = functions.storage.object().onFinalize(event => {
    const bucket = event.bucket;
    const contentType = event.contentType;
    const filePath = event.name;
    const origMetadata = event.metadata;
    const lang = path.dirname(filePath);
    const fileName = filePath.split("/").pop();

    console.log('XLSX to Firestore started.................................');


    const destinationBucket = gcs.bucket(bucket);
    const tempFilePath = path.join(os.tmpdir(), path.basename(filePath));
    const tempjsonPath = path.join(os.tmpdir(), "question.json");
    const metadata = { contentType: contentType, isCompressed: true };

//copy files to tmp(memory)
    return destinationBucket.file(filePath).download({
        destination: tempFilePath
    }).then(() => {
    	//xlsx to json
 		console.log("Creating the JSON");
	 	xlsx_json({
		  input: tempFilePath, 
		  output: tempjsonPath
		}, function(err, result) {
		  if(err) {
		    console.error(err);
		  }else {
		    var menu=result;
		    console.log(menu);
		    //write to firestore
			const db = admin.firestore();

		    console.log("Uploading to firestore");
			menu.forEach(function(obj) {
				if(obj.answer !== ""&&obj.question !== ""&&obj.optionA !== ""&&obj.optionB !== ""&&obj.optionC !== ""&&obj.optionD !== "") {
				
				    db.collection(lang).doc(fileName).collection("question").add({
				        ANSWER: obj.answer,
				        QUESTION: obj.question,
				        A: obj.optionA,
				        B: obj.optionB,
				        C: obj.optionC,
				        D: obj.optionD
				    }).catch(function(error) {
				        console.error("Error adding document: ", error);
				    });
				}

			});
			console.log('XLSX to Firestore Finished');

			//nofi to student
		    const payload = {
		        notification:{
			            title : 'New Question Paper Added',
			            body : 'Take the EXAM',
			            badge : '1',
			            sound : 'default'
		        }
		    };
		    return admin.database().ref('students-token').once('value').then(allToken => {
		        if(allToken.val()){
		            console.log('notification send successfully');
		            const token = Object.keys(allToken.val());
		            return admin.messaging().sendToDevice(token,payload);
		        }else{
		            console.log('No token available');
		        }
		        return null;
		    });
		    }
        });

	 });
});


