# AdvancedChatLog

AdvancedChatLog lets you view and search through the chat history, beyond what Minecraft usually lets you view

Please submit bugs to the [issue tracker](https://github.com/DarkKronicle/AdvancedChatLog/issues). Join the [Discord](https://discord.gg/WnaE3uZxDA) for more help!

## Dependencies

[AdvancedChatCore](https://github.com/DarkKronicle/AdvancedChatCore) is required to run the mod.

## Features

- View chat history beyond the vanilla Minecraft limit of 100 lines
- Search through chat history using RegEx, literal, and case-insensitive (UpperLower)
- Retain chat history across relaunching
- Smooth Scrolling

## Development

To develop, all dependencies should automatically be processed through gradle. To ensure code consistency the hook pre-commit.sh can be used. To install the pre-commit hook run:

`ln -s ../../pre-commit.sh .git/hooks/pre-commit`

To run spotless at any point it's recommended to execute `pre-commit.sh`.

## Credits n' more

Code & Mastermind: DarkKronicle

Language & Proofreading: Chronos22
