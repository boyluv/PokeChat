const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

const app = express()

app.get('/',
  (req, res) => {
    res.send("Hello super new World!!!!!!!!!!!!!")
  })

var pg = require('pg');

//Get all from database
app.get('/db', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query('SELECT * FROM test_table', function (err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {

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
      } //response.render('pages/db', {results: result.rows} ); }
    });
  });
});

//Get one with param from database
app.get('/db/:id', function (request, response) {
  const idUser = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query('SELECT * FROM test_table where id=' + idUser, function (err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          data: result.rows,
          message: 'Return test file'
        }));
      }
    });
  });
});


//Create new one from db
app.post('/db/add/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    id,
    name
  } = request.query

  // response.send(JSON.stringify({
  //   status: 'success', 
  //   data: name,
  //   message: 'Return test file'
  //   }));
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query("insert into test_table values (" + id + ",'" + name + "')", function (err, result) {

      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          message: 'Inserted'
        }));
      }
    });
  });
});


//Get One conservation with id
app.get('/convo/:id', function (request, response) {
  const convo_id = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT users.user_name,replies.rep_message FROM replies " +
      " INNER JOIN users ON replies.rep_by = users.user_id " +
      " WHERE related_to_convo = " + convo_id + " ORDER BY rep_id ASC ",
      function (err, result) {
        done();
        response.setHeader('Content-Type', 'application/json');
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: 'Request failed'
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Return test file'
          }));
        }
      });
  });
});

//Get all list conservation
app.get('/listconvo', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT users.user_name,replies.rep_message,replies.related_to_convo FROM users " +
      " INNER JOIN replies ON users.user_id = replies.rep_by " + 
      " ORDER BY rep_id ASC ",
      function (err, result) {
        done();
        response.setHeader('Content-Type', 'application/json');
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: 'Request failed'
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Return test file'
          }));
        }
      });
  });
});

//Get all user in system
app.get('/users', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT * FROM users ",
      function (err, result) {
        done();
        response.setHeader('Content-Type', 'application/json');
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: 'Request failed'
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Return test file'
          }));
        }
      });
  });
});

//Get all catergories in system
app.get('/categories', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT * FROM categories ",
      function (err, result) {
        done();
        response.setHeader('Content-Type', 'application/json');
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: 'Request failed'
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Return test file'
          }));
        }
      });
  });
});


//Insert new user
app.post('/user/add/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    id,
    name,
    pass,
    isUser
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "INSERT INTO users VALUES ("+id+",'"+name+"','e19d5cd5af0378da05f63f891c7467af','\\001')"
      , function (err, result) {

      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          message: 'Inserted'
        }));
      }
    });
  });
});

//Get user id if sign in success
//Insert new user
app.get('/user/login/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {name,pass} = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "SELECT user_id from users where user_name ='"+name+"' and user_pw='"+pass+"'"
      , function (err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err || result.rows) {
        response.send(JSON.stringify({
          status: 'error',
          isSignin: false,
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          data: result.rows,
          isSignin: true,
          message: 'Here is your id User'
        }));
      }
    });
  });
});

//Remove user with id
app.delete('/user/remove/:id', function (request, response) {
  const idUser = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // SELECT * FROM categories 
    // client.query('delete from users where user_id = ' + idUser, function (err, result) {    
    client.query('DELETE from users where user_id = ' + idUser, function (err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          data: result.rows,
          message: 'Remove success'
        }));
      }
    });
  });
});

//Insert new replies
app.post('/user/replies/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    rep_id,
    rep_message,
    related_to_convo,
    rep_by
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "INSERT INTO replies (rep_id,rep_message,related_to_convo,rep_by,rep_time) VALUES ("+rep_id+",'"+rep_message+"',"+related_to_convo+","+rep_by+",CURRENT_TIMESTAMP)"
      , function (err, result) {

      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          message: 'Inserted'
        }));
      }
    });
  });
});

//Remove message with id
app.delete('/replies/remove/:id', function (request, response) {
  const idRep = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // SELECT * FROM categories 
    // client.query('delete from users where user_id = ' + idUser, function (err, result) {    
    client.query('DELETE from replies where rep_id = ' + idRep, function (err, result) {
      done();
      response.setHeader('Content-Type', 'application/json');
      if (err) {
        response.send(JSON.stringify({
          status: 'error',
          data: err,
          message: 'Request failed'
        }));
        console.error(err);
        response.send("Error " + err);
      } else {
        response.send(JSON.stringify({
          status: 'success',
          data: result.rows,
          message: 'Remove success'
        }));
      }
    });
  });
});
app.listen(PORT, () => console.log('Example app listening on port 5000!'))