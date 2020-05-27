## Golang Starter Bot

See the [Go homepage](https://golang.org/) for language installation support.

### Environment Setup

- Go 1.14.2
- libtensorflow 1.15.0

If your submission has dependencies, they should be vendored using using [Go modules](https://github.com/golang/go/wiki/Modules). Your
submission must have `go.mod` and `go.sum` files included, even if you do not use any dependencies. The `go.sum` file will
pin dependencies to a fixed version, to ensure reproducible builds.
 
Begin a project with the command `go mod init author/projectname` in an empty directory. A Go program can then be written
as normal from `main.go`.

In `bot.json`, you should set the `botFilename` to whatever you called the project in your mod file.

##### Optional: Tensorflow

If you do not use Tensorflow in your bot submission, you *do not* need to configure this yourself.

The Dockerfile has libtensorflow-1.15.0 configured, so you can use the official [Tensorflow bindings](https://www.tensorflow.org/install/lang_go) 
by simply adding the dependency to your `go.mod` file via `go get`. If you wish to install them on your own environment, see
the above link for instructions. Note that the library is supported only on Linux and macOS X.


### Building and Running

The Dockerfile will build the bot as a step in building the Dockerfile, to the `/app` directory. It will produce a binary with the same name as the package.
For example, this bot will be built to an executable called `/app/golang_starterbot`. This executable can be run within the Docker container
with `./app/<botFilename>`.  

Bots are manually built as follows:
```
go build ./...
go build .
```
This will download any dependencies and build the program.