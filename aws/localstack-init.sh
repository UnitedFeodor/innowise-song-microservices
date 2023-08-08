#!/bin/bash
awslocal s3api create-bucket \
--bucket songfile \
--create-bucket-configuration LocationConstraint=eu-central-1

awslocal sqs create-queue --queue-name songfile-queue