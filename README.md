# simpleAutoMailer

Simple command-line tool to send emails.

SETUP: 
Configure sender account in mailer.properties and place in the same directory as the compiled jar.

ARGUMENTS:
  - -t: To field. Must be sepparated by semi-colon.
  - -s: Subject field.
  - -cc: CC (Carbon Copy) field.
  - -a: Path to attachments. In case of multiple attachments, separe it with spaces after this flag.
  - -b: Body field. Can be text formatted in HTML5 or plain text.
