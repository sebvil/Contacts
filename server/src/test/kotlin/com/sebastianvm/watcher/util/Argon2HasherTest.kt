package com.sebastianvm.watcher.util

import com.sebastianvm.watcher.testutils.baseTestSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

val Argon2HasherTest by baseTestSuite {
    testSuite("hash") {
        test("returns a different value than the input") {
            passwordHasher.hash("my-password") shouldNotBe "my-password"
        }

        test("produces different hashes for the same input due to salting") {
            passwordHasher.hash("my-password") shouldNotBe passwordHasher.hash("my-password")
        }
    }

    testSuite("verify") {
        test("returns true for correct password") {
            val hash = passwordHasher.hash("my-password")
            passwordHasher.verify("my-password", hash) shouldBe true
        }

        test("returns false for wrong password") {
            val hash = passwordHasher.hash("my-password")
            passwordHasher.verify("wrong-password", hash) shouldBe false
        }
    }
}
