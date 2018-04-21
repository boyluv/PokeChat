const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

const app = express()

const messFailed = 'Request failed'
const messNoData = 'There is no data'
const messSuccess = 'Request data success'

//Test example server
//Start
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

        response.send(JSON.stringify({
          status: 'success',
          data: result.rows,
          message: 'Return test file'
        }));
      } 
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

//Test example server
//End


//1--Get One conservation with  id
app.get('/convo/:id', function (request, response) {
  response.setHeader('Content-Type', 'application/json');  
  var convo_id = 1;  
  try{
    convo_id = parseInt(request.params.id);    
  }
  catch(err){
    response.send(JSON.stringify({
      status: 'error',
      data: err,
      message: messFailed
    }));
  }
  if(Number.isInteger(convo_id)){
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
              message: messFailed
            }));
            console.error(err);
            response.send("Error " + err);
          } else {
            if (result.rows.length == 0)
              response.send(JSON.stringify({
                status: 'success',
                data: result.rows,
                isEmpty: true,
                message: messNoData
              }));
            else
              response.send(JSON.stringify({
                status: 'success',
                data: result.rows,
                isEmpty: false,
                message: messSuccess
              }));
          }
        });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'Wrong input',
      message: messFailed
    }));
  }
  
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

//--Get all user Admin id
app.get('/admin', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "select user_id,cat_name,cat_description from users,categories where ref_cat_id != 1 and ref_cat_id = cat_id;",
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

//--Get all user Admin id
app.get('/checkconvo', function (request, response) {
  const {
    adminId,
    userId
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "select convo_id from users,conversations where convo_cat=ref_cat_id and user_id = " + adminId + " and convo_by = " + userId,
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
          if (result.rows.length == 0) {
            response.send(JSON.stringify({
              status: 'success',
              haveConnect: false,
              data: result.rows,
              message: 'Return test file'
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              haveConnect: true,
              data: result.rows,
              message: 'Return test file'
            }));
          }

        }
      });
  });
});


//--Get list conversation with categories with admin 
app.get('/listconvo/cate', function (request, response) {
  const {
    convo_cat
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "select user_name,rep_by,rep_message,convo_id from (select * from conversations, (select rep_id,rep_message,replies.ref_convo_id,rep_by,rep_time from replies, (select Max(rep_time) as time,ref_convo_id from replies group by ref_convo_id) as table2 where replies.rep_time = table2.time and replies.ref_convo_id = table2.ref_convo_id) as table3 where conversations.convo_id = table3.ref_convo_id) as table4,users where table4.convo_cat = " + convo_cat + " and users.user_id=table4.rep_by",
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
          if (result.rows.length == 0) {
            response.send(JSON.stringify({
              status: 'success',
              isEmpty: true,
              data: result.rows,
              message: 'Return test file'
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              isEmpty: false,
              message: 'Return test file'
            }));
          }
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


//--Get oppen key with user id
app.get('/user/pbkey/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    id
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "select pb_key from users where user_id = " + id,
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
            data: result.rows[0].pb_key,
            message: 'Inserted'
          }));
        }
      });
  });
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

//16 New
app.post('/convoadd', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    convo_cat,
    convo_by
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      " select * from conversations where convo_cat = " + convo_cat + " and convo_by = " + convo_by,
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
          if (result.rows.length == 0) {
            //Insert new value
            client.query(
              "INSERT INTO conversations (convo_cat,convo_by,convo_time) VALUES (" + convo_cat + "," + convo_by + ",CURRENT_TIMESTAMP)",
              function (err2, result2) {
                done();
                response.setHeader('Content-Type', 'application/json');
                if (err2) {
                  response.send(JSON.stringify({
                    status: 'error',
                    data: err2,
                    message: 'Request failed'
                  }));
                  console.error(err2);
                  response.send("Error " + err2);
                } else {
                  if (result2.rows.length > 0) {
                    response.send(JSON.stringify({
                      status: 'success',
                      message: 'Inserted inside'
                    }));
                  } else {
                    client.query(
                      "select * from conversations where convo_cat = " + convo_cat + " and convo_by = " + convo_by,
                      function (err3, result3) {
                        done();
                        response.setHeader('Content-Type', 'application/json');
                        if (err3) {
                          response.send(JSON.stringify({
                            status: 'error',
                            data: err3,
                            message: 'Request failed'
                          }));
                          console.error(err3);
                          response.send("Error " + err3);
                        } else {
                          response.send(JSON.stringify({
                            status: 'success',
                            data: result3.rows,
                            message: 'Here is your new inserted'
                          }));
                        }
                      });
                  }


                }
              });
          } else
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: 'Already have'
            }));

        }
      });
  });
});

//THEEND
//17 Add new categories for admin
//
app.post('/cate/add/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    cat_name,
    cat_description
  } = request.query
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "INSERT INTO categories (cat_name,cat_description) VALUES ('" + cat_name + "','" + cat_description + "')",
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
          pg.connect(process.env.DATABASE_URL, function (err, client, done) {
            client.query(
              "select * from categories where cat_name = '" + cat_name + "' and cat_description = '" + cat_description + "' ",
              function (err2, result2) {
                done();
                response.setHeader('Content-Type', 'application/json');
                if (err2) {
                  response.send(JSON.stringify({
                    status: 'error',
                    data: err2,
                    message: 'Request failed'
                  }));
                  console.error(err2);
                  response.send("Error " + err2);
                } else {
                  response.send(JSON.stringify({
                    status: 'success',
                    cat_id: result2.rows[0].cat_id,
                    message: 'Inserted'
                  }));
                }
              });
          });
        }
      });
  });
});


app.listen(PORT, () => console.log('Example app listening on port 5000!'))