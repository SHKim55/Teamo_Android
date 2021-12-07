package com.example.teamo_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "caubappul@gmail.com";
    String password = "bappul20";
    String code;

    public void sendSecurityCode(Context context, String sendTo){
        try{
            GMailSender gMailSender = new GMailSender(user, password);
            code = gMailSender.getEmailCode();
            gMailSender.sendMail("[인증] 티모 인증메일입니다.", "인증번호는 " + code + "입니다. 어플의 인증번호란에 입력해주세요.", sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.",
                    Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e){
            Log.i("send", e.toString());
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.",
                    Toast.LENGTH_SHORT).show();
        } catch (MessagingException e){
            Toast.makeText(context, "인터넷 연결을 확인해주십시오",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCode(){
        return code;
    }

}