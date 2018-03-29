const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

const app = express()

app.get('/',
(req, res) => {
    res.send("Hello super new World!")
        })

var pg = require('pg');

app.get('/db', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function(err, client, done) {
    client.query('SELECT * FROM test_table', function(err, result) {
      done();
      if (err)
       { console.error(err); response.send("Error " + err); }
      else
       {
           
    //     results.forEach(function(r) { 
    //         r.id  + r.name
    //  }); 

           response.send("Error " + result.rows[0].id  + " "+ result.rows[0].name); 
        }//response.render('pages/db', {results: result.rows} ); }
    });
  });
});        

app.listen(PORT, () => console.log('Example app listening on port 5000!'))  