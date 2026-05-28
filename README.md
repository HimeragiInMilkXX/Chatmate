# Pingmate / Chatmate

**Pingmate** は、「軽く一言 ping する」感覚で人とつながれる、シンプルなチャットアプリケーションです。  
同時にオンラインになっているユーザー同士が、気軽に会話できる場を提供することを目的としています。

## デモ動画

アプリの動作イメージは、以下の動画でご覧いただけます：  
https://youtu.be/wjZ-uTNG-Xg

## プロジェクトの背景

このプロジェクトの一番の目的は、初めて **AI コーディング（Codex）** を使ってライブラリを作ってみることにありました。  
本体のチャットアプリ Pingmate は、そのライブラリの上に構築した、用途のはっきりした軽量チャットアプリです。

ユーザーはアプリにアクセスし、利用可能な相手とリアルタイムにメッセージをやり取りできます。

## 使用技術

- フロントエンド：Nuxt, TypeScript, Nuxt UI, Tailwind CSS, SockJS  
- バックエンド：Spring Boot  
- AI エージェント：Codex  
- デザイン：Figma  

## EasyProfile について

**EasyProfile** は、AI により自動生成された Spring Boot 向けライブラリです。  
Sa-Token（https://github.com/dromara/sa-token）と連携し、認証機能とプロフィールの CRUD を毎回ゼロから実装せずに済むようにすることを目的としています。

特徴的なのは、**1 行の記述だけでユーザープロフィールの追加情報カラム（例：誕生日、自己紹介など）を定義し、保存・取得まで行える** 点です。  
実装の詳細と使い方は、`backend/easyprofile-auth-spring-boot-starter` 以下の AI 生成 README にまとまっています。

Pingmate は、この EasyProfile をバックエンドに組み込んだ **Easyprofile-demo-app** を土台として構築されています。
