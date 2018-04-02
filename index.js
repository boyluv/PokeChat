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

//1--Get One conservation with  id
app.get('/convo/:id', function (request, response) {
  const convo_id = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT users.user_name,replies.rep_message FROM replies " +
      " INNER JOIN users ON replies.rep_by = users.user_id " +
      " WHERE ref_convo_id = " + convo_id + " ORDER BY rep_id ASC ",
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

//2--Get all list conservation
app.get('/listconvo', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT users.user_name,replies.rep_message,replies.ref_convo_id FROM users " +
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

//3--Get all user in system
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

//4--Get all catergories in system
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

//5--Insert new user
app.post('/user/add/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    name,
    pass,
    pb_key,
    ref_cat_id
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "INSERT INTO users (user_name,user_pw,pb_key,ref_cat_id) VALUES ('" + name + "','" + pass + "','" + pb_key + "','" + ref_cat_id + "')",
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
            message: 'Inserted'
          }));
        }
      });
  });
});

//Get user id if sign in success
//6--
app.get('/user/login/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    name,
    pass
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "SELECT user_id,ref_cat_id from users where user_name ='" + name + "' and user_pw='" + pass + "'",
      function (err, result) {
        done();
        response.setHeader('Content-Type', 'application/json');
        if (err || result.rows.length < 1) {
          response.send(JSON.stringify({
            status: 'error',
            isSignin: false,
            data: err,
            message: 'Request failed'
          }));
          // console.error(err);
          // response.send("Error " + err);
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

//7--Remove user with id
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

//8--Insert new replies
app.post('/user/replies/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    rep_message,
    ref_convo_id,
    rep_by
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "INSERT INTO replies (rep_message,ref_convo_id,rep_by,rep_time) VALUES ('" + rep_message + "'," + ref_convo_id + "," + rep_by + ",CURRENT_TIMESTAMP)",
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
            message: 'Inserted'
          }));
        }
      });
  });
});

//9--Remove message with id
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

//10--Get key to crypto
app.get('/key', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  response.send(JSON.stringify({
    status: 'success',
    data: "pokeChat",
    message: 'Return key'
  }));
});


//11--Get all request with your id
app.get('/request/:id', function (request, response) {
  const id = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      " select * from request WHERE CAST(req_receiver AS integer)  = " + id,
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
          if (result.rows.length > 0)
            response.send(JSON.stringify({
              status: 'success',
              haveNotification: true,
              data: result.rows,
              message: 'Return test file'
            }));
          else
            response.send(JSON.stringify({
              status: 'success',
              haveNotification: false,
              data: result.rows,
              message: 'Return test file'
            }));
        }
      });
  });
});

//12--add new replies
app.post('/request/add', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    req_sender,
    req_receiver,
    message
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "INSERT INTO request (req_sender,req_receiver,message) VALUES (" + req_sender + "," + req_receiver + ",'" + message + "');",
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
            message: 'Inserted'
          }));
        }
      });
  });
});

//14--delete replies with id
app.delete('/request/remove/:id', function (request, response) {
  const idReq = parseInt(request.params.id);
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // SELECT * FROM categories 
    // client.query('delete from users where user_id = ' + idUser, function (err, result) {    
    client.query('DELETE FROM request WHERE CAST(req_receiver AS integer)  =' + idReq, function (err, result) {
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

//15--Add convo and return id , input categories and user id
//--Get all list conservation
app.get('/convo/add', function (request, response) {
  const {
    convo_by,
    convo_cat
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "select from conversations where convo_by = " + convo_by + " and convo_cat = " + convo_cat,
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
          if (result.rows.length > 0) {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: 'Return test file'
            }));
          } else {
            client.query(
              "INSERT INTO conversations (convo_cat,convo_by,convo_time) VALUES (" + convo_cat + "," + convo_by + ",CURRENT_TIMESTAMP);",
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
              }
            )
          }

        }
      });
  });
});





//4--Get all catergories in system
// app.get('/user/convo', function (request, response) {
//   pg.connect(process.env.DATABASE_URL, function (err, client, done) {
//     client.query(
//       "SELECT * FROM categories ",
//       function (err, result) {
//         done();
//         response.setHeader('Content-Type', 'application/json');
//         if (err) {
//           response.send(JSON.stringify({
//             status: 'error',
//             data: err,
//             message: 'Request failed'
//           }));
//           console.error(err);
//           response.send("Error " + err);
//         } else {
//           response.send(JSON.stringify({
//             status: 'success',
//             data: result.rows,
//             message: 'Return test file'
//           }));
//         }
//       });
//   });
// });
// app.get('/test', function (request, response) {
//   pg.connect(process.env.DATABASE_URL, function (err, client, done) {
//     client.query('SELECT * FROM user', function (err, result) {
//       done();
//       response.setHeader('Content-Type', 'application/json');
//       if (err) {
//         response.send(JSON.stringify({
//           status: 'error',
//           data: err,
//           message: 'Request failed'
//         }));
//         console.error(err);
//         response.send("Error " + err);
//       } else {
//         response.send(JSON.stringify({
//           status: 'success',
//           data: result.rows,
//           message: 'Return test file'
//         }));
//         // response.send("Error " + result.rows[0].id  + " "+ result.rows[0].name); 
//       } //response.render('pages/db', {results: result.rows} ); }
//     });
//   });
// });
app.listen(PORT, () => console.log('Example app listening on port 5000!'))