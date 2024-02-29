#include <cstdlib>
#include <iostream>

typedef enum {
	SUNDAY,
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY
} dow_t;

const char *map[] = {
	"SUNDAY   ",
	"MONDAY   ",
	"TUESDAY  ",
	"WEDNESDAY",
	"THURSDAY ",
	"FRIDAY   ",
	"SATURDAY "
};

int main ()
{
	int d = SUNDAY;
	std::cout << "Day-of-Week(DOW):\tCode" << std::endl;
	std::cout << "----------------------------" << std::endl;
	for (int i = 0; i != 7; ++i) {
		std::cout << map[i] << "\t" << ":" << "\t" << i << std::endl;
	}

	std::cout << "please enter a day code:";
	std::cin >> d;
	if (d < SUNDAY || d > SATURDAY) {
		std::cout << "you have entered an invalid day code" << std::endl;
		std::cout << "please input a code in the range 0 - 6 inclusive next time";
		std::cout << std::endl;
		exit(EXIT_FAILURE);
	}

	char busy = 0;
	const char msg_busy[] = "I have work to do I cannot go to the theather";
	const char msg_free[] = "I am free to go to the movie theater";
	// NOTE: using an array for mapping makes the code shorter
	switch (d) {
		case SUNDAY:
			std::cout << "do you have homework pending Y/N:";
			std::cin >> busy;
			if (busy != 'Y' && busy != 'N')  {
				std::cout << "invalid input" << std::endl;
				std::cout << "please try again later" << std::endl;
				exit(EXIT_FAILURE);
			}

			std::cout << "today is SUNDAY" << std::endl;

			if (busy == 'Y') {
				std::cout << msg_busy << std::endl;
			} else {
				std::cout << msg_free << std::endl;
			}

			break;
		case MONDAY:
			std::cout << "today is MONDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		case TUESDAY:
			std::cout << "today is TUESDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		case WEDNESDAY:
			std::cout << "today is WEDNESDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		case THURSDAY:
			std::cout << "today is THURSDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		case FRIDAY:
			std::cout << "today is FRIDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		case SATURDAY:
			std::cout << "today is SATURDAY" << std::endl;
			std::cout << msg_busy << std::endl;
			break;
		default:
			std::cout << "INVALID DAY CODE" << std::endl;
			exit(EXIT_FAILURE);
	}

	return 0;
}
