import bracket.Time
import bracket.createBracket

external fun require(module:String):dynamic


fun main(args: Array<String>) {
    val express = require("express")
    val app = express()
    val path  = require("path");

    val fs = require("fs")
    val __dirname = fs.realpathSync(".")

    app.set("views", path.join(__dirname, "/node/static/"))
    app.engine("html", require("ejs").renderFile)

    app.get("/times", { req, res ->
        res.type("text/html")
        val jsonArray: Array<dynamic> = require("./static/times.json")
        res.locals.data = jsonArray
        createBracket(jsonArray.map { Time(it.nome, it.codigo) }.toList())
        res.render("entidade.html")
    })

    app.get("/ligas", { req, res ->
        res.type("text/html")
        res.locals.data = require("./static/ligas.json")
        res.render("entidade.html")
    })

    app.get("/partidas", { req, res ->
        res.type("text/html")
        res.locals.data = require("./static/partidas.json")
        res.render("entidade.html")
    })

    app.listen(3000, {
        println("Listening on port 3000")
    })
}

