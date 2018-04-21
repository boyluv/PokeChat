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


//1--Get a conservation with id
app.get('/convo/:id', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  var convo_id = 1;
  try {
    convo_id = parseInt(request.params.id);
  } catch (err) {
    response.send(JSON.stringify({
      status: 'error',
      data: err,
      message: messFailed
    }));
  }
  if (Number.isInteger(convo_id)) {
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
  } else {
    response.send(JSON.stringify({
      status: 'error',
      data: 'Wrong input',
      message: messFailed
    }));
  }

});

//2--Get all list conservation
app.get('/listconvo', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT users.user_name,replies.rep_message,replies.ref_convo_id FROM users " +
      " INNER JOIN replies ON users.user_id = replies.rep_by " +
      " ORDER BY rep_id ASC ",
      function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: messSuccess
          }));
        }
      });
  });
});

//3--Get all user Admin detail
app.get('/admin', function (request, response) {
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    response.setHeader('Content-Type', 'application/json');
    client.query(
      "select user_id,cat_name,cat_description from users,categories where ref_cat_id != 1 and ref_cat_id = cat_id;",
      function (err, result) {
        done();
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
              message: messNoData
            }));
          else
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: messSuccess
            }));
        }
      });
  });
});

//4--Check connect conversation between user and admin
app.get('/checkconvo', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  const {
    adminId,
    userId
  } = request.query
  var adminIdValue = parseInt(adminId);
  var userIdValue = parseInt(userId);

  if (Number.isInteger(adminIdValue)) {
    if (Number.isInteger(userIdValue)) {
      pg.connect(process.env.DATABASE_URL, function (err, client, done) {
        client.query(
          "select convo_id from users,conversations where convo_cat=ref_cat_id and user_id = " + adminIdValue + " and convo_by = " + userIdValue,
          function (err, result) {
            done();
            if (err) {
              response.send(JSON.stringify({
                status: 'error',
                data: err,
                message: messFailed
              }));
              console.error(err);
              response.send("Error " + err);
            } else {
              if (result.rows.length == 0) {
                response.send(JSON.stringify({
                  status: 'success',
                  haveConnect: false,
                  data: result.rows,
                  message: messNoData
                }));
              } else {
                response.send(JSON.stringify({
                  status: 'success',
                  haveConnect: true,
                  data: result.rows,
                  message: messSuccess
                }));
              }

            }
          });
      });
    } else {
      response.send(JSON.stringify({
        status: 'success',
        haveConnect: false,
        data: 'user id is not number',
        message: messFailed
      }));
    }
  } else {
    response.send(JSON.stringify({
      status: 'success',
      haveConnect: false,
      data: 'admin id is not number',
      message: messFailed
    }));
  }

});


//5--Get list conversation with categories with admin 
app.get('/listconvo/cate', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  const {
    convo_cat
  } = request.query
  var convo_catValue = parseInt(convo_cat);
  if (Number.isInteger(convo_catValue)) {
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      client.query(
        "select user_name,rep_by,rep_message,convo_id from (select * from conversations, (select rep_id,rep_message,replies.ref_convo_id,rep_by,rep_time from replies, (select Max(rep_time) as time,ref_convo_id from replies group by ref_convo_id) as table2 where replies.rep_time = table2.time and replies.ref_convo_id = table2.ref_convo_id) as table3 where conversations.convo_id = table3.ref_convo_id) as table4,users where table4.convo_cat = " + convo_catValue + " and users.user_id=table4.rep_by",
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              isEmpty: true,
              data: err,
              message: messFailed
            }));
            console.error(err);
            response.send("Error " + err);
          } else {
            if (result.rows.length == 0) {
              response.send(JSON.stringify({
                status: 'success',
                isEmpty: true,
                data: result.rows,
                message: messNoData
              }));
            } else {
              response.send(JSON.stringify({
                status: 'success',
                data: result.rows,
                isEmpty: false,
                message: messSuccess
              }));
            }
          }
        });
    });
  } else {
    response.send(JSON.stringify({
      status: 'success',
      data: 'convo_cat is not number',
      isEmpty: true,
      message: messFailed
    }));
  }

});


//6--Get all user in system
app.get('/users', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT * FROM users ",
      function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          if (result.rows.length == 0) {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: messNoData
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: messSuccess
            }));
          }
        }
      });
  });
});

//7--Get all catergories in system
app.get('/categories', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    client.query(
      "SELECT * FROM categories ",
      function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
          console.error(err);
          response.send("Error " + err);
        } else {
          if (result.rows.length == 0) {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: messNoData
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows,
              message: messSuccess
            }));
          }

        }
      });
  });
});

//8--Insert new user
app.post('/user/add/', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  const {
    name,
    pass,
    pb_key,
    ref_cat_id
  } = request.query


  if (name && pass && pb_key && ref_cat_id && Number.isInteger(parseInt(ref_cat_id))) {
    //select user_id from users where user_name = 'test42dfsdfgsdfg' and user_pw = 'Hellosfdgdfgdf'
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      client.query(
        "select user_id from users where user_name = '" + name + "' and user_pw = '" + pass + "'",
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              isSinup: true,              
              data: err,
              message: messFailed
            }));
            console.error(err);
            response.send("Error " + err);
          } else {
            if (result.rows.length == 0) {
              pg.connect(process.env.DATABASE_URL, function (err, client, done) {
                client.query(
                  "INSERT INTO users (user_name,user_pw,pb_key,ref_cat_id) VALUES ('" + name + "','" + pass + "','" + pb_key + "','" + parseInt(ref_cat_id) + "')",
                  function (err, result) {
                    done();
                    if (err) {
                      response.send(JSON.stringify({
                        status: 'error why???',
                        isSinup: true,                        
                        data: err,
                        message: messFailed
                      }));
                      console.error(err);
                      response.send("Error " + err);
                    } else {
                      response.send(JSON.stringify({
                        status: 'success',
                        isSinup: false,
                        message: 'Inserted'
                      }));
                    }
                  });
              });
            } else {
              response.send(JSON.stringify({
                status: 'failed',
                isSinup: true,
                data: result.rows,
                message: 'User is already have'
              }));
            }


          }
        });
    });

  } else {

    response.send(JSON.stringify({
      status: 'success',
      isSinup: true,      
      data: 'Wrong input',
      message: messFailed
    }));
  }

});

//Get user id if sign in success
//9--
app.get('/user/login/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  const {
    name,
    pass
  } = request.query

  pg.connect(process.env.DATABASE_URL, function (err, client, done) {
    response.setHeader('Content-Type', 'application/json');    
    // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
    client.query(
      "SELECT user_id,ref_cat_id from users where user_name ='" + name + "' and user_pw='" + pass + "'",
      function (err, result) {
        done();
        if (err || result.rows.length < 1) {
          response.send(JSON.stringify({
            status: 'error',
            isSignin: false,
            data: err,
            message: messFailed
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

//10--Remove user with id
app.delete('/user/remove/:id', function (request, response) {
  response.setHeader('Content-Type', 'application/json');  
  const idUser = parseInt(request.params.id);
  if(Number.isInteger(idUser)){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      // SELECT * FROM categories 
      // client.query('delete from users where user_id = ' + idUser, function (err, result) {    
      client.query('DELETE from users where user_id = ' + idUser, function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Remove success'
          }));
        }
      });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//11--Insert new replies to conservation
app.post('/user/replies/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  response.setHeader('Content-Type', 'application/json');  
  const {
    rep_message,
    ref_convo_id,
    rep_by
  } = request.query

  if(rep_message && ref_convo_id && rep_by && Number.isInteger(parseInt(rep_by))  && Number.isInteger(parseInt(ref_convo_id))){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
      client.query(
        "INSERT INTO replies (rep_message,ref_convo_id,rep_by,rep_time) VALUES ('" + rep_message + "'," + parseInt(ref_convo_id) + "," + parseInt(rep_by) + ",CURRENT_TIMESTAMP)",
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              data: err,
              message: messFailed
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              message: 'Inserted'
            }));
          }
        });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//12--Remove message with id
app.delete('/replies/remove/:id', function (request, response) {
  response.setHeader('Content-Type', 'application/json');  
  const idRep = parseInt(request.params.id);
  if(Number.isInteger(idRep)){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      // SELECT * FROM categories 
      // client.query('delete from users where user_id = ' + idUser, function (err, result) {    
      client.query('DELETE from replies where rep_id = ' + idRep, function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Remove success'
          }));
        }
      });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//14--Get key to crypto
app.get('/key', function (request, response) {
  response.setHeader('Content-Type', 'application/json');
  response.send(JSON.stringify({
    status: 'success',
    data: "pokeChat",
    message: 'Return key'
  }));
});


//15--Get open public key with user id
app.get('/user/pbkey/', function (request, response) {
  // const idUser = parseInt(request.params.id);
  response.setHeader('Content-Type', 'application/json');  
  const {
    id
  } = request.query
  if(Number.isInteger(parseInt(id))){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      // client.query('insert into test_table values ('+id+', \''+name+'\')', function(err, result) {
      client.query(
        "select pb_key from users where user_id = " + parseInt(id),
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              data: err,
              message: messFailed
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              data: result.rows[0].pb_key,
              message: 'Inserted'
            }));
          }
        });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//16--Get all request with your id
app.get('/request/:id', function (request, response) {
  const id = parseInt(request.params.id);
  response.setHeader('Content-Type', 'application/json');
  if(Number.isInteger(id)){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      client.query(
        " select * from request WHERE CAST(req_receiver AS integer)  = " + id,
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              data: err,
              message: messFailed
            }));
          } else {
            if (result.rows.length > 0)
              response.send(JSON.stringify({
                status: 'success',
                haveNotification: true,
                data: result.rows,
                message: messSuccess
              }));
            else
              response.send(JSON.stringify({
                status: 'success',
                haveNotification: false,
                data: result.rows,
                message: messNoData
              }));
          }
        });
    });
  } 
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      haveNotification: false,      
      message: messFailed
    }));
  } 
  
});

//17--Add new replies
app.post('/request/add', function (request, response) {
  // const idUser = parseInt(request.params.id);
  response.setHeader('Content-Type', 'application/json');  
  const {
    req_sender,
    req_receiver,
    message
  } = request.query
  if(req_sender && req_receiver && message && Number.isInteger(parseInt(req_receiver)) && Number.isInteger(parseInt(req_sender))){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      client.query(
        "INSERT INTO request (req_sender,req_receiver,message) VALUES (" + parseInt(req_sender) + "," + parseInt(req_receiver) + ",'" + message + "');",
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              data: err,
              message: messFailed
            }));
          } else {
            response.send(JSON.stringify({
              status: 'success',
              message: 'Inserted'
            }));
          }
        });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//18--delete replies with id
app.delete('/request/remove/:id', function (request, response) {
  response.setHeader('Content-Type', 'application/json');  
  const idReq = parseInt(request.params.id);
  if(Number.isInteger(idReq)){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {  
      client.query('DELETE FROM request WHERE CAST(req_receiver AS integer)  =' + idReq, function (err, result) {
        done();
        if (err) {
          response.send(JSON.stringify({
            status: 'error',
            data: err,
            message: messFailed
          }));
        } else {
          response.send(JSON.stringify({
            status: 'success',
            data: result.rows,
            message: 'Remove success'
          }));
        }
      });
    });
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
});

//19 New
app.post('/convoadd', function (request, response) {
  // const idUser = parseInt(request.params.id);
  response.setHeader('Content-Type', 'application/json');  
  const {
    convo_cat,
    convo_by
  } = request.query

  var convo_catValue = parseInt(convo_cat)
  var convo_byValue = parseInt(convo_by)
  
  if(convo_catValue && convo_byValue && Number.isInteger(convo_byValue) && Number.isInteger(convo_catValue)){
    pg.connect(process.env.DATABASE_URL, function (err, client, done) {
      client.query(
        " select * from conversations where convo_cat = " + convo_catValue + " and convo_by = " + convo_byValue,
        function (err, result) {
          done();
          if (err) {
            response.send(JSON.stringify({
              status: 'error',
              data: err,
              message: messFailed
            }));
          } else {
            if (result.rows.length == 0) {
              //Insert new value
              client.query(
                "INSERT INTO conversations (convo_cat,convo_by,convo_time) VALUES (" + convo_catValue + "," + convo_byValue + ",CURRENT_TIMESTAMP)",
                function (err2, result2) {
                  done();
                  if (err2) {
                    response.send(JSON.stringify({
                      status: 'error',
                      data: err2,
                      message: messFailed
                    }));
                  } else {
                    if (result2.rows.length > 0) {
                      response.send(JSON.stringify({
                        status: 'success',
                        message: 'Inserted inside'
                      }));
                    } else {
                      client.query(
                        "select * from conversations where convo_cat = " + convo_catValue + " and convo_by = " + convo_byValue,
                        function (err3, result3) {
                          done();
                          if (err3) {
                            response.send(JSON.stringify({
                              status: 'error',
                              data: err3,
                              message: messFailed
                            }));
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
  }
  else{
    response.send(JSON.stringify({
      status: 'error',
      data: 'wrong input',
      message: messFailed
    }));
  }
  
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
            message: messFailed
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
                    message: messFailed
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