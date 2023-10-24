const express = require("express");
const bodyParser = require("body-parser");
const mysql = require("mysql2");
const e = require("express");
const app = express();

const conn = mysql.createConnection({
    database: "hr_v2",
    user: "root",
    password: "root",
    host: "localhost",
    port: 3306
});


    /*1)*/
    /*a) Lista de trabajadores de un tutor poniendo su id*/
    app.get('/listaTrabajadoresTutor/:idTutor', (req, res) => {
        const idTutor = req.params.idTutor;

        // Consulta SQL para obtener la lista de eventos ordenados por fecha y hora
        const query = `select concat( first_name," ",last_name) AS name, email, phone_number
                       from employees
                       where manager_id = ?;`;

        conn.query(query, [idTutor], (err, rows) => {
            if (err) {
                console.error(err);
                res.status(500).json({ error: 'Error en la consulta' });
            } else {
                res.json({ trabajadores: rows });
            }
        });
    });

    /*b) boton para descargar trabajador*/
    app.get('/consultaTrabajador/:idTrabajador', (req, res) => {
        const idTrabajador = req.params.idTrabajador;

        // Consulta SQL para obtener la lista de eventos ordenados por fecha y hora
        const query = `select concat( first_name," ",last_name) AS name, email, phone_number
                       from employees
                       where employee_id = ?;`;

        conn.query(query, [idTrabajador], (err, rows) => {
            if (err) {
                console.error(err);
                res.status(500).json({ error: 'Error en la consulta' });
            } else {
                res.json({ trabajador: rows });
            }
        });
    });

    /*c) asignar tutoria*/
    /*ejemplo de solicitud web: http://localhost:3000/actualizar-meeting?employeeId=102&managerId=100&meetingDate=2023-05-23 09:00:00*/

    app.get('/actualizar-meeting', (req, res) => {
        const employeeId = req.query.employeeId;
        const managerId = req.query.managerId;
        const meetingDate = req.query.meetingDate;

        if (!employeeId || !managerId || !meetingDate) {
            return res.status(400).json({ error: 'Faltan parámetros en la solicitud' });
        }

        // Primero, verifica si el empleado y el gerente están relacionados
        const checkQuery = 'SELECT CONCAT(first_name, " ", last_name) AS name FROM employees WHERE manager_id = ? AND employee_id = ?';

        conn.query(checkQuery, [managerId, employeeId], (error, results, fields) => {
            if (error) {
                return res.status(500).json({ error: 'Error al consultar la base de datos' });
            }

            if (results.length === 0) {
                return res.status(400).json({ error: 'El empleado y el gerente no están relacionados' });
            }

            // Luego, verifica si ya hay una fecha de reunión asignada
            const dateQuery = 'SELECT meeting_date FROM employees WHERE manager_id = ? AND employee_id = ?';

            conn.query(dateQuery, [managerId, employeeId], (dateError, dateResults, dateFields) => {
                if (dateError) {
                    return res.status(500).json({ error: 'Error al consultar la base de datos' });
                }

                if (dateResults.length === 1) {
                    // Finalmente, realiza la actualización
                    const updateQuery = 'UPDATE employees SET meeting_date = ? WHERE manager_id = ? AND employee_id = ?';

                    conn.query(updateQuery, [meetingDate, managerId, employeeId], (updateError, updateResults, updateFields) => {
                        if (updateError) {
                            return res.status(500).json({ error: 'Error al actualizar la base de datos' });
                        }

                        res.status(200).json({ message: 'Actualización exitosa' });
                    });
                }else{
                    console.log(dateResults.length)
                    console.log(dateResults)
                    return res.status(400).json({ error: 'Ya hay una reunión programada' });
                }


            });
        });
    });

    /*2)*/

    /*entrando a modo trabajador, te sale la notificacion si tiene una tutoría agendada con la fecha y horario*/

    app.get('/tutoriaAgendada/:idTrabajador', (req, res) => {
        const idTrabajador = req.params.idTrabajador;

        // Consulta SQL para obtener la lista de eventos ordenados por fecha y hora
        const query = `select meeting_date from employees where employee_id =?;`;

        conn.query(query, [idTrabajador], (err, rows) => {
            if (err) {
                console.error(err);
                res.status(500).json({ error: 'Error en la consulta' });
            } else {
                res.json({ meetingDate: rows });
            }
        });
    });

    /*a) se descarga la imagen que esta en las indicaciones xd*/

    /*b) ingresar feedback de longitud maxima 250 caracteres del employe con id x */
    /*ejemplo de solicitud web: http://localhost:3000/ingresarFeedback?employeeId=102&feedback=hola*/

    app.get('/ingresarFeedback', (req, res) => {
        const employeeId = req.query.employeeId;
        const feedback = req.query.feedback;

        if (!employeeId || !feedback ) {
            return res.status(400).json({ error: 'Faltan parámetros en la solicitud' });
        }

        const checkQuery = 'select meeting_date from employees where employee_id=?;';

        conn.query(checkQuery, [employeeId], (error, results, fields) => {

            if (error) {
                return res.status(500).json({ error: 'Error al consultar la base de datos' });
            }

            if (results.length === 0) {
                return res.status(400).json({ error: 'Aun no tiene la tutoria' });
            }

            const updateQuery = 'UPDATE employees SET employee_feedback = ? WHERE (employee_id = ?);';

            conn.query(updateQuery, [feedback, employeeId], (updateError, updateResults, updateFields) => {
                if (updateError) {
                    return res.status(500).json({ error: 'Error al actualizar la base de datos' });
                }

                res.status(200).json({ message: 'Actualización exitosa' });
            });

        });
    });

    app.listen(3000, function () {
        console.log("servidor iniciado correctamente en el puerto 3000");
    })