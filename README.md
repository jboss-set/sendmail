# SendMail

Simple Java CLI application that sends a file as an email.

## Example Usage

Currently only single use case is supported, sending content file as an email body:  

```shell
java -jar sendmail-<version>-jar-with-dependencies.jar \
  --content message.html \
  --content-type text/html \
  --subject 'Email subject' \
  --from sender@example.org \
  --to recipient@example.org \
  --smtp-server smtp.server.example.org \
  --smtp-port 587
```

To show the usage message run with `-h` or `--help` option.

## To Do

* Support reading email body from stdin.
* Support more SMTP options, like authentication and such. Currently only unauthenticated connections with StartTLS 
  enabled are supported.