package kraski.server

import kraski.common.models.participants.*
import java.awt.Color
import java.awt.Font
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.imageio.ImageIO
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.reflect.KClass

class MailBuilder {
    companion object {
        fun a(href: String, text: String) = "<a href='$href'>$text</a>"
        const val br = "<br/>"
        fun little(text: String) = "<span style='fontSize: 12pt'>$text</span>"
    }
}

fun sendCertificate(formType: FormType, m: Participant, mClass: KClass<out AnyForm>) {
    val fio = when (m) {
        is OnlyName -> m.surname + " " + m.name
        is NameOrGroup -> if (m.group != "") m.group else m.surname + " " + m.name
        else -> error("must be name")
    }

    val email = m.email
    val hash = m.time


    val mailProps = Properties()
    mailProps["mail.smtp.host"] = "smtp.yandex.ru"
    mailProps["mail.smtp.auth"] = "true"
    mailProps["mail.smtp.port"] = "465"
    mailProps["mail.smtp.socketFactory.port"] = "465"
    mailProps["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"

    val mailSession = Session.getInstance(mailProps, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("kraski-chuvashii", "iamnotadoctor2472")
        }
    })

    val message = MimeMessage(mailSession)
    message.setFrom(InternetAddress("kraski-chuvashii@yandex.ru"))
    message.setRecipients(Message.RecipientType.TO, email.trim().toLowerCase())
    message.setSubject("Сертификат участника", "UTF-8")
    val mp = MimeMultipart()

    val mbpText = MimeBodyPart()
    mbpText.setContent(MailBuilder.run {
        "Здравствуйте! Ваша работа успешно загружена. Спасибо вам за участие в конкурсе!"
    }, "text/html; charset=utf-8")
    mp.addBodyPart(mbpText)

    val bufferedImage = ImageIO.read(File("images/design/cbase.jpg"))
    val graphics = bufferedImage.graphics
    graphics.color = Color.black

    val fioList = mutableListOf<String>()

    var s = ""
    fio.split(' ').forEach { word ->
        graphics.font = Font("Arial Black", Font.BOLD, 60)
        val fm = graphics.fontMetrics
        if (fm.stringWidth(s + word) > 1300) {
            fioList += s.trim()
            s = ""
        }
        s += "$word "
    }
    fioList += s


    val nomination = formType.title
    val nominationList = mutableListOf<String>()

    s = ""
    nomination.split(' ').forEach { word ->
        graphics.font = Font("Arial Black", Font.PLAIN, 40)
        val fm = graphics.fontMetrics
        if (fm.stringWidth(s + word) > 1300) {
            nominationList += s.trim()
            s = ""
        }
        s += "$word "
    }
    nominationList += s


//    val supervisorFioList = mutableListOf<String>()
//    if (supervisorFIO != null) {
//        graphics.font = Font("Arial Black", Font.BOLD, 40)
//        val fm = graphics.fontMetrics
//        s = ""
//        ("(Куратор: $supervisorFIO)").split(' ').forEach { word ->
//            if (fm.stringWidth(s + word) > 1300) {
//                supervisorFioList += s.trim()
//                s = ""
//            }
//            s += "$word "
//        }
//        supervisorFioList += s
//    }


    fioList.forEachIndexed { index, str ->
        graphics.font = Font("Arial Black", Font.BOLD, 60)
        val fm = graphics.fontMetrics
//        val otherHeight = supervisorFioList.size * 40
        val otherHeight = 0
        val fioWidth = fm.stringWidth(str)
        graphics.drawString(str, 827 - fioWidth / 2,
                950 - 30 * (fioList.size - 1) + 60 * index - otherHeight / 2)
    }

    nominationList.forEachIndexed { index, str ->
        graphics.font = Font("Arial Black", Font.PLAIN, 40)
        val fm = graphics.fontMetrics
//        val otherHeight = supervisorFioList.size * 40
        val otherHeight = 0
        val fioWidth = fm.stringWidth(str)
        graphics.drawString(str, 827 - fioWidth / 2,
                1450 - 20 * (fioList.size - 1) + 40 * index - otherHeight / 2)
    }

//    supervisorFioList.forEachIndexed { index, str ->
//        graphics.font = Font("Arial Black", Font.PLAIN, 40)
//        val fm = graphics.fontMetrics
//        val otherHeight = fioList.size * 60
//        val sFioWidth = fm.stringWidth(str)
//        graphics.drawString(str, 827 - sFioWidth / 2,
//            900 - 20 * (supervisorFioList.size - 1) + 40 * index + otherHeight / 2)
//    }

//    graphics.font = Font("Arial Black", Font.BOLD, 31)
//    graphics.drawString(number, 1313, 490)
    val fileC = File("images/certs/cert-$hash.jpg")
    ImageIO.write(bufferedImage, "jpg", fileC)
    println("Image Created")


    val mbpImage = MimeBodyPart()
    val fds = FileDataSource(fileC)
    mbpImage.dataHandler = DataHandler(fds)
    mbpImage.fileName = fds.name
    mp.addBodyPart(mbpImage)

    message.setContent(mp)
    message.sentDate = Date()
    message.saveChanges()
    Transport.send(message)
    println("The EmailListener says: ```, that the letter have been sent to the email called the $email```")
}