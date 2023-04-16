# MovieController (기본 기능) API 목록

<details>
<summary>API 상세 설명 및 쿼리</summary>

## 1. 영화 목록 조회

> 전체 영화 목록을 조회합니다. 연관된 영화 이미지나 비디오도 전부 하나의 쿼리로 조회됩니다.

### 쿼리 예시

>    select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.in_use=?

- API 경로: `http://localhost:8080/api/v1/movies`
- HTTP 메서드: GET
- 응답 예시 (Json Body):

````
[
    {
        "id": 1,
        "releaseDate": 1,
        "movieName": "Movie 1",
        "genre": "Genre 1",
        "director": "Director 1",
        "postImageUrl": "poster_1",
        "movieImages": [
            {
                "id": 1,
                "imageUrl": "image_1_for_movie_1",
                "movieId": 1
            },
            {
                "id": 2,
                "imageUrl": "image_2_for_movie_1",
                "movieId": 1
            },
            {
                "id": 3,
                "imageUrl": "image_3_for_movie_1",
                "movieId": 1
            },
            {
                "id": 4,
                "imageUrl": "image_4_for_movie_1",
                "movieId": 1
            },
            {
                "id": 5,
                "imageUrl": "image_5_for_movie_1",
                "movieId": 1
            }
        ],
        "movieVideos": [
            {
                "id": 1,
                "videoUrl": "video_1_for_movie_1",
                "movieId": 1
            },
            {
                "id": 2,
                "videoUrl": "video_2_for_movie_1",
                "movieId": 1
            },
            {
                "id": 3,
                "videoUrl": "video_3_for_movie_1",
                "movieId": 1
            }
        ]
    },
    ...
]
````

- 응답:
  - 성공(200 OK): 영화 목록 조회 결과를 담은 MovieResponseDto 리스트
  - 실패(204 No Content): 조회된 영화 목록이 없는 경우

## 2. 영화 상세 조회

> 하나의 영화를 조회합니다. 연관된 이미지나 비디오도 하나의 쿼리로 조회됩니다.

### 쿼리 예시

>    select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?



- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`
- HTTP 메서드: GET
- 응답 예시(Json Body):

````
{
    "id": 3,
    "releaseDate": 3,
    "movieName": "Movie 3",
    "genre": "Genre 3",
    "director": "Director 3",
    "postImageUrl": "poster_3",
    "movieImages": [
        {
            "id": 11,
            "imageUrl": "image_1_for_movie_3",
            "movieId": 3
        },
        {
            "id": 12,
            "imageUrl": "image_2_for_movie_3",
            "movieId": 3
        },
        {
            "id": 13,
            "imageUrl": "image_3_for_movie_3",
            "movieId": 3
        },
        {
            "id": 14,
            "imageUrl": "image_4_for_movie_3",
            "movieId": 3
        },
        {
            "id": 15,
            "imageUrl": "image_5_for_movie_3",
            "movieId": 3
        }
    ],
    "movieVideos": [
        {
            "id": 7,
            "videoUrl": "video_1_for_movie_3",
            "movieId": 3
        },
        {
            "id": 8,
            "videoUrl": "video_2_for_movie_3",
            "movieId": 3
        },
        {
            "id": 9,
            "videoUrl": "video_3_for_movie_3",
            "movieId": 3
        }
    ]
}
````

- 응답:
    - 성공(200 OK): 영화 상세 정보를 담은 MovieResponseDto
    - 실패(404 Not Found): 해당하는 영화가 없는 경우

## 3. 영화 생성

> 영화를 생성합니다.

### 쿼리 예시
>     insert 
    into
        movie
        (id, director, genre, in_use, movie_name, poster_image_url, release_date) 
    values
        (default, ?, ?, ?, ?, ?, ?)

- API 경로: `http://localhost:8080/api/v1/movies`
- HTTP 메서드: POST
- 요청 예시:

````
POST /api/v1/movies
{
  "releaseDate": 0,
  "movieName": "DummyName",
  "genre": "DummyGenre",
  "director": "DummyDirector",
  "postImageUrl": "DummyUrl"
}

````

- 응답:
  - 성공(201 Created): 생성된 영화의 상세 정보를 담은 MovieResponseDto와 생성된 영화의 URI
  - 실패(400 Bad Request): 올바르지 않은 요청 형식인 경우

## 4. 영화 수정

> 영화의 세부 정보를 수정합니다. SELECT 쿼리로 찾아온 후 UPDATE 쿼리가 발생합니다.

### 쿼리 예시
> Hibernate:
select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?


> Hibernate:
update
movie
set
director=?,
genre=?,
in_use=?,
movie_name=?,
poster_image_url=?,
release_date=?
where
id=?

- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`
- HTTP 메서드: PUT
- 요청 예시:

````
{
  "releaseDate": 0,
  "movieName": "DummyName",
  "genre": "DummyGenre",
  "director": "DummyDirector",
  "postImageUrl": "DummyUrl"
}
````

- 응답:
  - 성공(200 OK): 영화 수정 성공
  - 실패(404 Not Found): 해당하는 영화가 없는 경우

## 5. 영화 삭제

> 영화를 삭제합니다. 연관된 엔티티들(이미지, 비디오) 도 모두 한 쿼리로 삭제됩니다.
> 해당 영화 조회용 쿼리 1개 + 삭제용 쿼리 3개가 발생합니다. (영화, 영화이미지, 영화비디오)

### 쿼리 예시

> Hibernate:
select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m1_0.movie_name,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
where
m1_0.id=?

> Hibernate:
delete
from
movie_video
where
movie_id=movie_id


> Hibernate:
delete
from
movie_image
where
movie_id=movie_id

> Hibernate:
delete
from
movie
where
id=?

- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`
- HTTP 메서드: DELETE
- 응답:
  - 성공(200 OK): 영화 삭제 성공
  - 실패(404 Not Found): 해당하는 영화가 없는 경우


</details>

---

# MyMovieController (추가 기능들) API 목록


<details>
<summary>API 상세 설명 및 쿼리</summary>


## 1. 페이징을 통한 영화 조회

> 페이징을 통해서 10개씩 영화를 조회합니다.

### 쿼리 예시

> select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.in_use=?


- API 경로 : 'http://localhost:8080/api/v1/movies/pages/{pageNum}'
- HTTP 매서드 : GET
- 응답 예시(Json Body)

````
http://localhost:8080/api/v1/movies/pages/3 으로 요청
-> 21~30 사이 Id 가진 엔티티 조회
[
    {
        "id": 21,
        "releaseDate": 0,
        "movieName": "DummyName",
        "genre": "DummyGenre",
        "director": "DummyDirector",
        "postImageUrl": "DummyUrl",
        "movieImages": [],
        "movieVideos": []
    },
    {
        "id": 22,
        "releaseDate": 0,
        "movieName": "DummyName",
        "genre": "DummyGenre",
        "director": "DummyDirector",
        "postImageUrl": "DummyUrl",
        "movieImages": [],
        "movieVideos": []
    },
    {
        "id": 23,
        "releaseDate": 0,
        "movieName": "DummyName",
        "genre": "DummyGenre",
        "director": "DummyDirector",
        "postImageUrl": "DummyUrl",
        "movieImages": [],
        "movieVideos": []
    }
]

````

- 응답:
  - 성공(200 OK): 영화 목록 조회 결과를 담은 MovieResponseDto 리스트
  - 실패(204 No Content): 조회된 영화 목록이 없는 경우

# 2. 동적 조회 조건을 사용한 영화 조회

> 동적 조회 조건(영화 이름, 감독 이름) 을 활용한 조회입니다

### 쿼리 예시

> m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.movie_name like ? escape '!'
and m1_0.in_use=?


- API 경로 : 'http://localhost:8080/api/v1/movies/pages/{pageNum}'
- HTTP 매서드 : GET
- 요청 예시
````
1. 영화 제목으로만 조회
{
  "movieName": "Movie"
}

2. 감독으로만 조회
{
  "director": "Director"
}

3. 두 조건에 모두 맞는 데이터 조회
{
  "movieName": "Movie",
  "director": "Director"
}

````
- 응답:
  - 성공(200 OK): 영화 목록 조회 결과를 담은 MovieResponseDto 리스트
  - 실패(400 BAD REQUEST): RequestBody 형태의 Json 데이터가 올바르지 않은 경우

# 3. 논리 삭제

> Movie 엔티티에 설정한 inUse 테이블을 이용해서, 논리 삭제합니다.

> 조회 후 UPDATE 쿼리가 발생합니다.

### 쿼리 예시

> Hibernate:
select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?

>Hibernate:
update
movie
set
director=?,
genre=?,
in_use=?,
movie_name=?,
poster_image_url=?,
release_date=?
where
id=?


- API 경로 : 'http://localhost:8080/api/v1/movies/{movieId}'
- HTTP 매서드 : Patch
- 응답:
  - 성공(200 OK): 영화 삭제 성공
  - 실패(404 Not Found): 해당하는 영화가 없는 경우

</details>