package com.ztiany.androidipc;

interface ISecurityCenter {

    String encrypt(String content);

    String decrypt(String pwd);

}