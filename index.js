const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

const app = express()

app.get('/',
(req, res) => {
    res.send("Hello super new World!")
        })

var pg = require('pg');

//Get all from database
app.get('/db', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function(err, client, done) {
    client.query('SELECT * FROM test_table', function(err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err)
       { 
        response.send(JSON.stringify({
          status: 'error', 
          data: err,
          message: 'Request failed'
          }));
         console.error(err); response.send("Error " + err); 
        }
      else
       {
           
    //     results.forEach(function(r) { 
    //         r.id  + r.name
    //  }); 
    // response.status(200)
    // .json({
    //   status: 'success',
    //   data: result.row[0],
    //   message: 'Retrieved ALL puppies'
    // });
    
    response.send(JSON.stringify({
      status: 'success', 
      data: result.rows,
      message: 'Return test file'
      }));
          // response.send("Error " + result.rows[0].id  + " "+ result.rows[0].name); 
        }//response.render('pages/db', {results: result.rows} ); }
    });
  });
});        

//Get one with param from database
app.get('/a', function (request, response) {
  // const id = req.params.id
  // const {lat1,lat2,lng1,lng2} = req.query
  const {namid} = req.query
  // var nameId = parseInt(req.query.id);
  pg.connect(process.env.DATABASE_URL, function(err, client, done) {
    client.query('SELECT * FROM test_table where id=5', function(err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err)
       { 
        response.send(JSON.stringify({
          status: 'error', 
          data: err,
          message: 'Request failed'+namid
          }));
         console.error(err); response.send("Error " + err); 
        }
      else
       {
           
    //     results.forEach(function(r) { 
    //         r.id  + r.name
    //  }); 
    // response.status(200)
    // .json({
    //   status: 'success',
    //   data: result.row[0],
    //   message: 'Retrieved ALL puppies'
    // });
    
    response.send(JSON.stringify({
      status: 'success', 
      data: result.rows,
      message: 'Return test file'
      }));
          // response.send("Error " + result.rows[0].id  + " "+ result.rows[0].name); 
        }//response.render('pages/db', {results: result.rows} ); }
    });
  });
});        

app.listen(PORT, () => console.log('Example app listening on port 5000!'))  